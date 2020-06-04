package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.ShoppingCart;
import com.haotang.pet.swipelistview.SwipeListView;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.AlertDialogEditText;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ExtendedEditText;
import com.haotang.pet.view.TagTextView;
import com.haotang.pet.view.ViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/8/31 15:54
 */
public class ShoppingCartAdapter<T> extends CommonAdapter<T> {
    private final SwipeListView slv_header_shoppingcart;
    private boolean etHasFocus;
    private int localNum;
    private AlertDialogEditText builder;

    public ShoppingCartAdapter(Activity mContext, List<T> mDatas, SwipeListView slv_header_shoppingcart) {
        super(mContext, mDatas);
        this.slv_header_shoppingcart = slv_header_shoppingcart;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ShoppingCart shoppingCart = (ShoppingCart) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_shoppingcat, position);
        View vw_item_shopcart_top = viewHolder
                .getView(R.id.vw_item_shopcart_top);
        View vw_item_shopcart_fenge = viewHolder
                .getView(R.id.vw_item_shopcart_fenge);
        LinearLayout ll_item_shopcart_select = viewHolder
                .getView(R.id.ll_item_shopcart_select);
        ImageView iv_item_shopcart_topselect = viewHolder
                .getView(R.id.iv_item_shopcart_topselect);
        TextView tv_item_shopcart_classname = viewHolder
                .getView(R.id.tv_item_shopcart_classname);
        ImageView iv_item_shopcart_select = viewHolder
                .getView(R.id.iv_item_shopcart_select);
        ImageView iv_item_shopcart_shopimg = viewHolder
                .getView(R.id.iv_item_shopcart_shopimg);
        TagTextView tv_item_shopcart_title = viewHolder
                .getView(R.id.tv_item_shopcart_title);
        TextView tv_item_shopcart_shopgg = viewHolder
                .getView(R.id.tv_item_shopcart_shopgg);
        TextView tv_item_shopcart_noshop = viewHolder
                .getView(R.id.tv_item_shopcart_noshop);
        LinearLayout ll_item_shopcart_tiaozheng = viewHolder
                .getView(R.id.ll_item_shopcart_tiaozheng);
        ImageView iv_item_shopcart_shopjian = viewHolder
                .getView(R.id.iv_item_shopcart_shopjian);
        final EditText et_item_shopcart_shopnum = viewHolder
                .getView(R.id.et_item_shopcart_shopnum);
        ImageView iv_item_shopcart_shopjia = viewHolder
                .getView(R.id.iv_item_shopcart_shopjia);
        TextView tv_item_shopcart_shopprice = viewHolder
                .getView(R.id.tv_item_shopcart_shopprice);
        RelativeLayout rl_item_shopcart_back = viewHolder
                .getView(R.id.rl_item_shopcart_back);
        Button btn_item_shopcart_delete = viewHolder
                .getView(R.id.btn_item_shopcart_delete);
        LinearLayout ll_item_shopcart_front = viewHolder
                .getView(R.id.ll_item_shopcart_front);
        LinearLayout ll_item_shopcart_detail = viewHolder
                .getView(R.id.ll_item_shopcart_detail);
        TextView tv_item_shopcart_shopbuzu = viewHolder
                .getView(R.id.tv_item_shopcart_shopbuzu);
        ll_item_shopcart_front.bringToFront();
        if (shoppingCart != null) {
            if (shoppingCart.getePrice() > 0) {
                SpannableString ss = new SpannableString("¥" + shoppingCart.getePrice());
                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
                        1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tv_item_shopcart_shopprice.setText(ss);
            } else {
                SpannableString ss = new SpannableString("¥" + shoppingCart.getRetailPrice());
                ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
                        1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tv_item_shopcart_shopprice.setText(ss);
            }
            if (shoppingCart.getClassIndex() == -1) {
                vw_item_shopcart_top.setVisibility(View.VISIBLE);
                ll_item_shopcart_select.setVisibility(View.VISIBLE);
                if (shoppingCart.isClassSelect()) {
                    iv_item_shopcart_topselect.setImageResource(R.drawable.complaint_reason);
                } else {
                    iv_item_shopcart_topselect.setImageResource(R.drawable.complaint_reason_disable);
                }
            } else {
                vw_item_shopcart_top.setVisibility(View.GONE);
                ll_item_shopcart_select.setVisibility(View.GONE);
            }
            if (shoppingCart.isSelect()) {
                iv_item_shopcart_select.setImageResource(R.drawable.complaint_reason);
            } else {
                iv_item_shopcart_select.setImageResource(R.drawable.complaint_reason_disable);
            }
            tv_item_shopcart_shopbuzu.setVisibility(View.GONE);
            if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                tv_item_shopcart_noshop.setVisibility(View.GONE);
                ll_item_shopcart_tiaozheng.setVisibility(View.VISIBLE);
                if (shoppingCart.getComCount() < shoppingCart.getStock()) {
                    if (shoppingCart.getComCount() < 1) {
                        iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial);
                        iv_item_shopcart_shopjia.setClickable(true);
                        iv_item_shopcart_shopjia.setEnabled(true);
                        iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial);
                        iv_item_shopcart_shopjian.setClickable(false);
                        iv_item_shopcart_shopjian.setEnabled(false);
                    } else if (shoppingCart.getComCount() == 1) {
                        iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial);
                        iv_item_shopcart_shopjia.setClickable(true);
                        iv_item_shopcart_shopjia.setEnabled(true);
                        iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial);
                        iv_item_shopcart_shopjian.setClickable(false);
                        iv_item_shopcart_shopjian.setEnabled(false);
                    } else {
                        iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial);
                        iv_item_shopcart_shopjia.setClickable(true);
                        iv_item_shopcart_shopjia.setEnabled(true);
                        iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial);
                        iv_item_shopcart_shopjian.setClickable(true);
                        iv_item_shopcart_shopjian.setEnabled(true);
                    }
                } else if (shoppingCart.getComCount() == shoppingCart.getStock()) {
                    if (shoppingCart.getComCount() == 1) {
                        iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_item_shopcart_shopjia.setClickable(false);
                        iv_item_shopcart_shopjia.setEnabled(false);
                        iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial);
                        iv_item_shopcart_shopjian.setClickable(false);
                        iv_item_shopcart_shopjian.setEnabled(false);
                    } else {
                        iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                        iv_item_shopcart_shopjia.setClickable(false);
                        iv_item_shopcart_shopjia.setEnabled(false);
                        iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial);
                        iv_item_shopcart_shopjian.setClickable(true);
                        iv_item_shopcart_shopjian.setEnabled(true);
                    }
                } else {
                    Utils.setText(tv_item_shopcart_shopbuzu, shoppingCart.getCommodityText(), "", View.VISIBLE, View.GONE);
                    iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                    iv_item_shopcart_shopjia.setClickable(false);
                    iv_item_shopcart_shopjia.setEnabled(false);
                    iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial);
                    iv_item_shopcart_shopjian.setClickable(true);
                    iv_item_shopcart_shopjian.setEnabled(true);
                }
            } else {
                Utils.setText(tv_item_shopcart_noshop, shoppingCart.getCommodityText(), "", View.VISIBLE, View.GONE);
                ll_item_shopcart_tiaozheng.setVisibility(View.GONE);
                iv_item_shopcart_shopjia.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial);
                iv_item_shopcart_shopjia.setClickable(false);
                iv_item_shopcart_shopjia.setEnabled(false);
                iv_item_shopcart_shopjian.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial);
                iv_item_shopcart_shopjian.setClickable(false);
                iv_item_shopcart_shopjian.setEnabled(false);
            }
            GlideUtil.loadImg(mContext, shoppingCart.getThumbNail(), iv_item_shopcart_shopimg,
                    R.drawable.icon_production_default);
            Utils.setText(tv_item_shopcart_classname, shoppingCart.getClassName(), "", View.VISIBLE, View.VISIBLE);
            if (Utils.isStrNull(shoppingCart.getMarketTag())) {
                tv_item_shopcart_title.setSingleTagAndContent(shoppingCart.getMarketTag(), shoppingCart.getTitle());
            } else {
                tv_item_shopcart_title.setText(shoppingCart.getTitle());
            }
            Utils.setText(tv_item_shopcart_shopgg, "规格:" + shoppingCart.getSpecName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(et_item_shopcart_shopnum, shoppingCart.getComCount() + "", "", View.VISIBLE, View.VISIBLE);
            btn_item_shopcart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopCartDeleteListener != null) {
                        onShopCartDeleteListener.OnShopCartDelete(position);
                    }
                }
            });
            iv_item_shopcart_topselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopCartClassSelectListener != null) {
                        onShopCartClassSelectListener.OnShopCartClassSelect(position);
                    }
                }
            });
            iv_item_shopcart_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopCartSelectListener != null) {
                        onShopCartSelectListener.OnShopCartSelect(position);
                    }
                }
            });
            iv_item_shopcart_shopjian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopCartReduceListener != null && shoppingCart.getComCount() > 1) {
                        onShopCartReduceListener.OnShopCartReduce(position);
                    }
                }
            });
            iv_item_shopcart_shopjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShopCartIncreaseListener != null && shoppingCart.getComCount() < shoppingCart.getStock()) {
                        onShopCartIncreaseListener.OnShopCartIncrease(position);
                    }
                }
            });
            ll_item_shopcart_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", shoppingCart.getCommodityId()));
                }
            });
            et_item_shopcart_shopnum.setFocusable(false);
            if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                et_item_shopcart_shopnum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder = new AlertDialogEditText(mContext).builder();
                        final ExtendedEditText et_item_shopcart_shopnum1 = builder.getEt_item_shopcart_shopnum();
                        final ImageView iv_item_shopcart_shopjia1 = builder.getIv_item_shopcart_shopjia();
                        final ImageView iv_item_shopcart_shopjian1 = builder.getIv_item_shopcart_shopjian();
                        //et_item_shopcart_shopnum1.setFilters(new InputFilter[]{new InputFilterMinMax(1, shoppingCart.getStock())});
                        if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                            if (shoppingCart.getComCount() < shoppingCart.getStock()) {
                                if (shoppingCart.getComCount() < 1) {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(true);
                                    iv_item_shopcart_shopjia1.setEnabled(true);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(false);
                                    iv_item_shopcart_shopjian1.setEnabled(false);
                                } else if (shoppingCart.getComCount() == 1) {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(true);
                                    iv_item_shopcart_shopjia1.setEnabled(true);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(false);
                                    iv_item_shopcart_shopjian1.setEnabled(false);
                                } else {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(true);
                                    iv_item_shopcart_shopjia1.setEnabled(true);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(true);
                                    iv_item_shopcart_shopjian1.setEnabled(true);
                                }
                            } else if (shoppingCart.getComCount() == shoppingCart.getStock()) {
                                if (shoppingCart.getComCount() == 1) {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(false);
                                    iv_item_shopcart_shopjia1.setEnabled(false);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(false);
                                    iv_item_shopcart_shopjian1.setEnabled(false);
                                } else {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(false);
                                    iv_item_shopcart_shopjia1.setEnabled(false);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(true);
                                    iv_item_shopcart_shopjian1.setEnabled(true);
                                }
                            } else {
                                iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                iv_item_shopcart_shopjia1.setClickable(false);
                                iv_item_shopcart_shopjia1.setEnabled(false);
                                iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                iv_item_shopcart_shopjian1.setClickable(true);
                                iv_item_shopcart_shopjian1.setEnabled(true);
                            }
                        } else {
                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                            iv_item_shopcart_shopjia1.setClickable(false);
                            iv_item_shopcart_shopjia1.setEnabled(false);
                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                            iv_item_shopcart_shopjian1.setClickable(false);
                            iv_item_shopcart_shopjian1.setEnabled(false);
                        }
                        et_item_shopcart_shopnum1.setText(String.valueOf(shoppingCart.getComCount()));
                        if (Utils.isStrNull(et_item_shopcart_shopnum1.getText().toString())) {
                            et_item_shopcart_shopnum1.setSelection(et_item_shopcart_shopnum1.getText().toString().length());
                        }
                        et_item_shopcart_shopnum1.setFocusable(true);
                        builder.setTitle("修改购买数量");
                        localNum = shoppingCart.getComCount();
                        iv_item_shopcart_shopjia1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                localNum++;
                                if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                                    if (shoppingCart.getComCount() < shoppingCart.getStock()) {
                                        if (shoppingCart.getComCount() < 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else if (shoppingCart.getComCount() == 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(true);
                                            iv_item_shopcart_shopjian1.setEnabled(true);
                                        }
                                    } else if (shoppingCart.getComCount() == shoppingCart.getStock()) {
                                        if (shoppingCart.getComCount() == 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(false);
                                            iv_item_shopcart_shopjia1.setEnabled(false);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(false);
                                            iv_item_shopcart_shopjia1.setEnabled(false);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(true);
                                            iv_item_shopcart_shopjian1.setEnabled(true);
                                        }
                                    } else {
                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                        iv_item_shopcart_shopjia1.setClickable(false);
                                        iv_item_shopcart_shopjia1.setEnabled(false);
                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                        iv_item_shopcart_shopjian1.setClickable(true);
                                        iv_item_shopcart_shopjian1.setEnabled(true);
                                    }
                                } else {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(false);
                                    iv_item_shopcart_shopjia1.setEnabled(false);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(false);
                                    iv_item_shopcart_shopjian1.setEnabled(false);
                                }
                                et_item_shopcart_shopnum1.setText(String.valueOf(localNum));
                                if (Utils.isStrNull(et_item_shopcart_shopnum1.getText().toString())) {
                                    et_item_shopcart_shopnum1.setSelection(et_item_shopcart_shopnum1.getText().toString().length());
                                }
                            }
                        });
                        iv_item_shopcart_shopjian1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                localNum--;
                                if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                                    if (shoppingCart.getComCount() < shoppingCart.getStock()) {
                                        if (shoppingCart.getComCount() < 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else if (shoppingCart.getComCount() == 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(true);
                                            iv_item_shopcart_shopjia1.setEnabled(true);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(true);
                                            iv_item_shopcart_shopjian1.setEnabled(true);
                                        }
                                    } else if (shoppingCart.getComCount() == shoppingCart.getStock()) {
                                        if (shoppingCart.getComCount() == 1) {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(false);
                                            iv_item_shopcart_shopjia1.setEnabled(false);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(false);
                                            iv_item_shopcart_shopjian1.setEnabled(false);
                                        } else {
                                            iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                            iv_item_shopcart_shopjia1.setClickable(false);
                                            iv_item_shopcart_shopjia1.setEnabled(false);
                                            iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                            iv_item_shopcart_shopjian1.setClickable(true);
                                            iv_item_shopcart_shopjian1.setEnabled(true);
                                        }
                                    } else {
                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                        iv_item_shopcart_shopjia1.setClickable(false);
                                        iv_item_shopcart_shopjia1.setEnabled(false);
                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                        iv_item_shopcart_shopjian1.setClickable(true);
                                        iv_item_shopcart_shopjian1.setEnabled(true);
                                    }
                                } else {
                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                    iv_item_shopcart_shopjia1.setClickable(false);
                                    iv_item_shopcart_shopjia1.setEnabled(false);
                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                    iv_item_shopcart_shopjian1.setClickable(false);
                                    iv_item_shopcart_shopjian1.setEnabled(false);
                                }
                                et_item_shopcart_shopnum1.setText(String.valueOf(localNum));
                                if (Utils.isStrNull(et_item_shopcart_shopnum1.getText().toString())) {
                                    et_item_shopcart_shopnum1.setSelection(et_item_shopcart_shopnum1.getText().toString().length());
                                }
                            }
                        });
                        et_item_shopcart_shopnum1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    //获得焦点才添加监听
                                    et_item_shopcart_shopnum1.addTextChangedListener(new TextWatcher() {

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before,
                                                                  int count) {

                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                                      int after) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Log.e("TAG", "差让他打破特然 afterTextChanged");
                                            if (s != null) {
                                                if (Utils.isStrNull(s.toString())) {
                                                    if (Utils.isStrNull(s.toString().trim())) {
                                                        Pattern p = Pattern.compile("[0-9]*");
                                                        Matcher m = p.matcher(s.toString().trim());
                                                        if (m.matches()) {
                                                            int num = Integer.parseInt(s.toString().trim());
                                                            if (shoppingCart.getStock() > 0 && shoppingCart.getStatus() != 3) {
                                                                if (num < shoppingCart.getStock()) {
                                                                    if (num < 1) {
                                                                        localNum = 1;
                                                                        ToastUtil.showToastShortBottom(mContext, "数量不能小于1");
                                                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                                                        iv_item_shopcart_shopjia1.setEnabled(true);
                                                                        iv_item_shopcart_shopjia1.setClickable(true);
                                                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                                                        iv_item_shopcart_shopjian1.setEnabled(false);
                                                                        iv_item_shopcart_shopjian1.setClickable(false);
                                                                    } else if (num == 1) {
                                                                        localNum = num;
                                                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                                                        iv_item_shopcart_shopjia1.setEnabled(true);
                                                                        iv_item_shopcart_shopjia1.setClickable(true);
                                                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                                                        iv_item_shopcart_shopjian1.setEnabled(false);
                                                                        iv_item_shopcart_shopjian1.setClickable(false);
                                                                    } else {
                                                                        localNum = num;
                                                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_avial_dialog);
                                                                        iv_item_shopcart_shopjian1.setEnabled(true);
                                                                        iv_item_shopcart_shopjian1.setClickable(true);
                                                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                                                        iv_item_shopcart_shopjian1.setEnabled(true);
                                                                        iv_item_shopcart_shopjian1.setClickable(true);
                                                                    }
                                                                } else if (num == shoppingCart.getStock()) {
                                                                    if (num == 1) {
                                                                        localNum = num;
                                                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                                                        iv_item_shopcart_shopjia1.setClickable(false);
                                                                        iv_item_shopcart_shopjia1.setEnabled(false);
                                                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                                                        iv_item_shopcart_shopjian1.setClickable(false);
                                                                        iv_item_shopcart_shopjian1.setEnabled(false);
                                                                    } else {
                                                                        localNum = shoppingCart.getStock();
                                                                        iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                                                        iv_item_shopcart_shopjia1.setClickable(false);
                                                                        iv_item_shopcart_shopjia1.setEnabled(false);
                                                                        iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                                                        iv_item_shopcart_shopjian1.setClickable(true);
                                                                        iv_item_shopcart_shopjian1.setEnabled(true);
                                                                    }
                                                                } else {
                                                                    localNum = shoppingCart.getStock();
                                                                    ToastUtil.showToastShortBottom(mContext, "数量不能大于库存");
                                                                    iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                                                    iv_item_shopcart_shopjia1.setClickable(false);
                                                                    iv_item_shopcart_shopjia1.setEnabled(false);
                                                                    iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_avial_dialog);
                                                                    iv_item_shopcart_shopjian1.setClickable(true);
                                                                    iv_item_shopcart_shopjian1.setEnabled(true);
                                                                }
                                                            } else {
                                                                localNum = 0;
                                                                //ToastUtil.showToastShortBottom(mContext, "库存为0");
                                                                iv_item_shopcart_shopjia1.setImageResource(R.drawable.icon_item_shopcart_shopjia_notavial_dialog);
                                                                iv_item_shopcart_shopjia1.setClickable(false);
                                                                iv_item_shopcart_shopjia1.setEnabled(false);
                                                                iv_item_shopcart_shopjian1.setImageResource(R.drawable.icon_item_shopcart_shopjian_notavial_dialog);
                                                                iv_item_shopcart_shopjian1.setClickable(false);
                                                                iv_item_shopcart_shopjian1.setEnabled(false);
                                                            }
                                                            et_item_shopcart_shopnum1.removeTextChangedListener(this);
                                                            et_item_shopcart_shopnum1.setText(localNum + "");
                                                            et_item_shopcart_shopnum1.addTextChangedListener(this);
                                                            if (Utils.isStrNull(et_item_shopcart_shopnum1.getText().toString())) {
                                                                et_item_shopcart_shopnum1.setSelection(et_item_shopcart_shopnum1.getText().toString().length());
                                                            }
                                                        } else {
                                                            ToastUtil.showToastShortBottom(mContext, "请输入数字");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    //失去焦点时清除监听
                                    et_item_shopcart_shopnum1.clearTextChangedListeners();
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onShopSerchListener != null && Utils.isStrNull(et_item_shopcart_shopnum1.getText().toString().trim())) {
                                    onShopSerchListener.OnShopSerch(shoppingCart.getCartId(), Integer.parseInt(et_item_shopcart_shopnum1.getText().toString().trim()));
                                    builder.dismiss();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        builder.show();
                    }
                });
            }
        }
        return viewHolder.getConvertView();
    }

    public OnShopSerchListener onShopSerchListener = null;

    public interface OnShopSerchListener {
        public void OnShopSerch(int cartId, int shopNum);
    }

    public void setOnShopSerchListener(OnShopSerchListener onShopSerchListener) {
        this.onShopSerchListener = onShopSerchListener;
    }

    public OnShopCartClassSelectListener onShopCartClassSelectListener = null;

    public interface OnShopCartClassSelectListener {
        public void OnShopCartClassSelect(int position);
    }

    public void setOnShopCartClassSelectListener(OnShopCartClassSelectListener onShopCartClassSelectListener) {
        this.onShopCartClassSelectListener = onShopCartClassSelectListener;
    }

    public OnShopCartSelectListener onShopCartSelectListener = null;

    public interface OnShopCartSelectListener {
        public void OnShopCartSelect(int position);
    }

    public void setOnShopCartSelectListener(OnShopCartSelectListener onShopCartSelectListener) {
        this.onShopCartSelectListener = onShopCartSelectListener;
    }

    public OnShopCartIncreaseListener onShopCartIncreaseListener = null;

    public interface OnShopCartIncreaseListener {
        public void OnShopCartIncrease(int position);
    }

    public void setOnShopCartIncreaseListener(OnShopCartIncreaseListener onShopCartIncreaseListener) {
        this.onShopCartIncreaseListener = onShopCartIncreaseListener;
    }

    public OnShopCartReduceListener onShopCartReduceListener = null;

    public interface OnShopCartReduceListener {
        public void OnShopCartReduce(int position);
    }

    public void setOnShopCartReduceListener(OnShopCartReduceListener onShopCartReduceListener) {
        this.onShopCartReduceListener = onShopCartReduceListener;
    }

    public OnShopCartDeleteListener onShopCartDeleteListener = null;

    public interface OnShopCartDeleteListener {
        public void OnShopCartDelete(int position);
    }

    public void setOnShopCartDeleteListener(OnShopCartDeleteListener onShopCartDeleteListener) {
        this.onShopCartDeleteListener = onShopCartDeleteListener;
    }

}