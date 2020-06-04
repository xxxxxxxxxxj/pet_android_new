package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.CommodityDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Goods;
import com.haotang.pet.entity.ShopMallGoods;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/9/5 18:04
 */
public class ShopMallGoodsAdapter<T> extends CommonAdapter<T> {
    public ShopMallGoodsAdapter(Activity mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        try {
            final ShopMallGoods shopMallGoods = (ShopMallGoods) mDatas.get(position);
            viewHolder = ViewHolder.get(mContext, convertView, parent,
                    R.layout.item_shopmallgoods_adapter, position);
            ImageView iv_itemsopmallgoods1 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods1);
            ImageView iv_itemsopmallgoods2 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods2);
            ImageView iv_itemsopmallgoods3 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods3);
            ImageView iv_itemsopmallgoods4 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods4);
            ImageView iv_itemsopmallgoods5 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods5);
            ImageView iv_itemsopmallgoods6 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods6);
            ImageView iv_itemsopmallgoods7 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods7);
            ImageView iv_itemsopmallgoods8 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods8);
            ImageView iv_itemsopmallgoods9 = viewHolder
                    .getView(R.id.iv_itemsopmallgoods9);
            View vw_shoppingmallfrag_jkhh = viewHolder
                    .getView(R.id.vw_shoppingmallfrag_jkhh);
            iv_itemsopmallgoods1.bringToFront();
            if (shopMallGoods != null) {
                GlideUtil.loadImg(mContext, shopMallGoods.getTitlePicDomain(), iv_itemsopmallgoods1,
                        R.drawable.icon_production_default);
                List<Goods> goodsList = shopMallGoods.getGoodsList();
                if (goodsList != null) {
                    if (goodsList.size() > 0) {
                        final Goods goods = goodsList.get(0);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods2,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 1) {
                        final Goods goods = goodsList.get(1);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods3,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 2) {
                        final Goods goods = goodsList.get(2);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods4,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 3) {
                        final Goods goods = goodsList.get(3);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods5,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }

                                }
                            });
                        }
                    }
                    if (goodsList.size() > 4) {
                        final Goods goods = goodsList.get(4);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods6,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 5) {
                        final Goods goods = goodsList.get(5);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods7,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods7.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 6) {
                        final Goods goods = goodsList.get(6);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods8,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods8.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                    if (goodsList.size() > 7) {
                        final Goods goods = goodsList.get(7);
                        if (goods != null) {
                            GlideUtil.loadImg(mContext, goods.getPic(), iv_itemsopmallgoods9,
                                    R.drawable.icon_production_default);
                            iv_itemsopmallgoods9.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Integer.parseInt(goods.getPoint()) == 20) {
                                        casePosion(position, Integer.parseInt(goods.getBackup()));
                                    } else {
                                        Utils.goService(mContext, Integer.parseInt(goods.getPoint()), goods.getBackup());
                                    }
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder.getConvertView();
    }

    public void casePosion(int position, int backup) {
        switch (position) {
            case 0:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallFrag_ClassOne);
                mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", backup).putExtra("source", Global.SOURCE_MALLCLASSONE));
                break;
            case 1:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallFrag_ClassTwo);
                mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", backup).putExtra("source", Global.SOURCE_MALLCLASSTwo));
                break;
            case 2:
                UmengStatistics.UmengEventStatistics(mContext, Global.UmengEventID.click_MallFrag_ClassThree);
                mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", backup).putExtra("source", Global.SOURCE_MALLCLASSThree));
                break;
            default:
                mContext.startActivity(new Intent(mContext, CommodityDetailActivity.class).putExtra("commodityId", backup).putExtra("source", Global.SOURCE_MALLCLASSThree));
                break;
        }
    }
}