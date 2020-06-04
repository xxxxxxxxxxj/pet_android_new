package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haotang.pet.LoginNewActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceNewActivity;
import com.haotang.pet.ShopDetailActivity;
import com.haotang.pet.entity.ShopListBean;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.GlideUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/10/24
 * @Description:门店列表适配器
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private Activity context;
    private List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> list;
    private PopupWindow pWin;
    private double addr_lat;
    private double addr_lng;
    private int previous;

    public ShopListAdapter(Activity context, List<ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean> list, double addr_lat, double addr_lng, int previous) {
        this.context = context;
        this.list = list;
        this.addr_lat = addr_lat;
        this.addr_lng = addr_lng;
        this.previous = previous;
    }

    @Override
    public ShopListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopListAdapter.MyViewHolder holder, final int position) {
        if (Utils.isStrNull(list.get(position).getTag())) {
            holder.tv_item_shop_list_tag.setVisibility(View.VISIBLE);
            holder.tv_item_shop_list_tag.bringToFront();
            holder.tv_item_shop_list_tag.setText(list.get(position).getTag());
        } else {
            holder.tv_item_shop_list_tag.setVisibility(View.GONE);
        }
        if (Utils.isStrNull(list.get(position).getShopActiveTitle())) {
            holder.ll_item_shop_list_activity.setVisibility(View.VISIBLE);
            holder.tv_item_shop_list_activity.setText(list.get(position).getShopActiveTitle() + " 查看详情 >");
        } else {
            holder.ll_item_shop_list_activity.setVisibility(View.GONE);
        }
        GlideUtil.loadRoundImg(context, list.get(position).getImg(),
                holder.imageview_item_shop_list_icon,
                R.drawable.icon_production_default, 8);
        Utils.setText(holder.textView_item_shop_list_name,
                list.get(position).getShopName(), "", View.VISIBLE, View.INVISIBLE);
        Utils.setText(holder.textView_item_shop_list_dist, list.get(position).getDist(), "", View.VISIBLE, View.INVISIBLE);
        Utils.setText(holder.textView_item_shop_list_yysj, "营业时间: " + list.get(position).getOpenTime(), "", View.VISIBLE, View.INVISIBLE);
        Utils.setText(holder.textView_item_shop_list_address,
                "地址：" + list.get(position).getAddress(), "", View.VISIBLE, View.INVISIBLE);
        if (Utils.isStrNull(list.get(position).getNotOpen())) {
            holder.button_item_shop_list_go_appointment.setText("即将开业");
            holder.button_item_shop_list_go_appointment.setTextSize(12);
            holder.textView_item_shop_list_name.setMaxWidth(DensityUtil.dp2px(context, 110));
        } else {
            holder.button_item_shop_list_go_appointment.setText("选我");
            holder.button_item_shop_list_go_appointment.setTextSize(15);
            holder.textView_item_shop_list_name.setMaxWidth(DensityUtil.dp2px(context, 150));
        }
        holder.ll_item_shop_list_activity
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).getShopActiveTitle() != null && !list.get(position).getShopActiveTitle().equals("")) {
                            Utils.goService((Activity) context, list.get(position).getShopActivePoint(), list.get(position).getShopActiveBackup());
                        }
                    }
                });
        holder.iv_item_shop_list_phone
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.setPhoneDialog(context, list.get(position).getPhone(), list.get(position).getPhone(), "取消", "呼叫",
                                context.getResources().getColor(R.color.a666666), context.getResources().getColor(R.color.a007AFF),
                                context.getResources().getColor(R.color.a333333));
                    }
                });
        holder.button_item_shop_list_go_appointment
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isStrNull(list.get(position).getNotOpen())) {
                            if (list.get(position).getHotelImg() != null
                                    && list.get(position).getHotelImg().size() > 0) {
                                showPop(list.get(position).getHotelImg().get(0));
                            }
                        } else {
                            if (!Utils.checkLogin(context)) {
                                context.startActivity(new Intent(context, LoginNewActivity.class));
                                return;
                            }
                            if (previous == Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST
                                    || previous == Global.SERVICEFEATURE_TO_PETLIST) {
                                Intent intent = new Intent(context, ServiceNewActivity.class);
                                intent.putExtra("cityId", list.get(position).getCityId());
                                intent.putExtra("shopid", list.get(position).getId());
                                intent.putExtra("shopname", list.get(position).getShopName());
                                intent.putExtra("shopimg", list.get(position).getImg());
                                intent.putExtra("shopaddr", list.get(position).getAddress());
                                intent.putExtra("lat", list.get(position).getLat());
                                intent.putExtra("shopPhone", list.get(position).getPhone());
                                intent.putExtra("lng", list.get(position).getLng());
                                intent.putExtra("tag", list.get(position).getTag());
                                intent.putExtra("openTime", list.get(position).getOpenTime());
                                intent.putExtra("dist", list.get(position).getDist());
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("shopid", list.get(position).getId());
                                intent.putExtra("cityId", list.get(position).getCityId());
                                intent.putExtra("shopname", list.get(position).getShopName());
                                intent.putExtra("shopPhone", list.get(position).getPhone());
                                intent.putExtra("shopimg", list.get(position).getImg());
                                intent.putExtra("shopaddr", list.get(position).getAddress());
                                intent.putExtra("lat", list.get(position).getLat());
                                intent.putExtra("lng", list.get(position).getLng());
                                intent.putExtra("tag", list.get(position).getTag());
                                intent.putExtra("openTime", list.get(position).getOpenTime());
                                intent.putExtra("dist", list.get(position).getDist());
                                context.setResult(Global.RESULT_OK, intent);
                                context.finish();
                            }
                        }

                    }
                });
        holder.service_go_home_detail
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isStrNull(list.get(position).getNotOpen())) {
                            if (list.get(position).getHotelImg() != null
                                    && list.get(position).getHotelImg().size() > 0) {
                                showPop(list.get(position).getHotelImg().get(0));
                            }
                        } else {
                            if (!Utils.checkLogin(context)) {
                                context.startActivity(new Intent(context, LoginNewActivity.class));
                                return;
                            }
                            Intent intent = new Intent(context, ShopDetailActivity.class);
                            intent.putExtra("shopid", list.get(position).getId());
                            intent.putExtra("addr_lat", addr_lat);
                            intent.putExtra("shopPhone", list.get(position).getPhone());
                            intent.putExtra("cityId", list.get(position).getCityId());
                            intent.putExtra("addr_lng", addr_lng);
                            context.startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public long getHeaderId(int position) {
        Log.e("TAG", "getHeaderId");
        long headerId = 0;
        if (list != null && list.size() > 0) {
            headerId = list.get(position).getHeaderId();
        }
        return headerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        Log.e("TAG", "onCreateHeaderViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shopadapter_headview, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("TAG", "onBindHeaderViewHolder");
        TextView textView = (TextView) holder.itemView;
        if (list != null && list.size() > 0) {
            ShopListBean.DataBean.RegionsBean.RegionMapBean.ShopsBean shopsBean = list.get(position);
            if (shopsBean != null) {
                textView.setVisibility(View.VISIBLE);
                Log.e("TAG", "shopsBean.getRegion() = " + shopsBean.getRegion());
                Utils.setText(textView, shopsBean.getRegion(), "", View.VISIBLE, View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview_item_shop_list_icon;
        TextView textView_item_shop_list_name;
        TextView textView_item_shop_list_address;
        Button button_item_shop_list_go_appointment;
        LinearLayout service_go_home_detail;
        TextView tv_item_shop_list_tag;
        LinearLayout ll_item_shop_list_activity;
        ImageView iv_item_shop_list_phone;
        ImageView iv_item_shop_list_jjky;
        TextView tv_item_shop_list_activity;
        TextView textView_item_shop_list_yysj;
        TextView textView_item_shop_list_dist;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageview_item_shop_list_icon = itemView
                    .findViewById(R.id.imageview_item_shop_list_icon);
            textView_item_shop_list_name = itemView
                    .findViewById(R.id.textView_item_shop_list_name);
            textView_item_shop_list_address = itemView
                    .findViewById(R.id.textView_item_shop_list_address);
            button_item_shop_list_go_appointment = itemView
                    .findViewById(R.id.button_item_shop_list_go_appointment);
            service_go_home_detail = itemView
                    .findViewById(R.id.service_go_home_detail);
            tv_item_shop_list_tag = itemView
                    .findViewById(R.id.tv_item_shop_list_tag);
            ll_item_shop_list_activity = itemView
                    .findViewById(R.id.ll_item_shop_list_activity);
            iv_item_shop_list_phone = itemView
                    .findViewById(R.id.iv_item_shop_list_phone);
            tv_item_shop_list_activity = itemView
                    .findViewById(R.id.tv_item_shop_list_activity);
            textView_item_shop_list_yysj = itemView
                    .findViewById(R.id.textView_item_shop_list_yysj);
            textView_item_shop_list_dist = itemView
                    .findViewById(R.id.textView_item_shop_list_dist);
        }
    }

    private void showPop(String imgUrl) {
        pWin = null;
        if (pWin == null) {
            View view = View.inflate(context, R.layout.not_practice, null);
            ImageView imageView_not_practice = (ImageView) view
                    .findViewById(R.id.imageView_not_practice);
            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setBackgroundDrawable(new BitmapDrawable());
            pWin.setOutsideTouchable(true);
            pWin.setFocusable(true);
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            pWin.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
            Utils.setAttribute(context, pWin);
            GlideUtil.loadImg(context, imgUrl, imageView_not_practice, 0);
            imageView_not_practice.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (pWin.isShowing()) {
                        pWin.dismiss();
                        pWin = null;
                    }
                    Utils.onDismiss(context);
                }
            });
            pWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    Utils.onDismiss(context);
                }
            });
        }
    }
}
