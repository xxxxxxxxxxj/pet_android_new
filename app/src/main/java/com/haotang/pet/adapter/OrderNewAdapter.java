package com.haotang.pet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.AppointMentActivity;
import com.haotang.pet.EvaluateNewActivity;
import com.haotang.pet.FosterEvaluteActivity;
import com.haotang.pet.FosterHomeActivity;
import com.haotang.pet.FosterLiveActivity;
import com.haotang.pet.FosterOrderDetailNewActivity;
import com.haotang.pet.MApplication;
import com.haotang.pet.R;
import com.haotang.pet.UpdateOrderConfirmNewActivity;
import com.haotang.pet.WashOrderDetailActivity;
import com.haotang.pet.entity.Order;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.apache.http.Header;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-12-19 17:58
 */
public class OrderNewAdapter extends BaseQuickAdapter<Order, BaseViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private final static long DAYTIMEINMILLS = 86400000;
    private final Activity mActivity;

    public OrderNewAdapter(int layoutResId, List<Order> data, Activity mActivity) {
        super(layoutResId, data);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Order item) {
        TextView tv_orderitem_addrtype = helper.getView(R.id.tv_orderitem_addrtype);
        TextView tv_orderitem_servicefee = helper.getView(R.id.tv_orderitem_servicefee);
        TextView tv_orderitem_pickup = helper.getView(R.id.tv_orderitem_pickup);
        TextView tv_orderitem_servicename = helper.getView(R.id.tv_orderitem_servicename);
        TextView tv_orderitem_starttime = helper.getView(R.id.tv_orderitem_starttime);
        TextView textView_right_status = helper.getView(R.id.textView_right_status);
        RecyclerView rv_item_order_pet = helper.getView(R.id.rv_item_order_pet);
        TextView tv_item_order_dian = helper.getView(R.id.tv_item_order_dian);
        CountdownView tv_item_order_time = helper.getView(R.id.tv_item_order_time);
        TextView tv_orderitem_fwmd = helper.getView(R.id.tv_orderitem_fwmd);
        TextView tv_item_order_qxdd = helper.getView(R.id.tv_item_order_qxdd);
        RelativeLayout rl_orderitem_jynum = helper.getView(R.id.rl_orderitem_jynum);
        TextView tv_orderitem_totalday = helper.getView(R.id.tv_orderitem_totalday);
        TextView tv_orderitem_pingjia = helper.getView(R.id.tv_orderitem_pingjia);
        TextView textview_gratuity = helper.getView(R.id.textview_gratuity);
        LinearLayout ll_item_order_root = helper.getView(R.id.ll_item_order_root);
        TextView textview_appen_single = helper.getView(R.id.textview_appen_single);
        ImageView iv_orderitem_dzf = helper.getView(R.id.iv_orderitem_dzf);
        if (item != null) {
            if (item.petImgList.size() > 0) {
                rv_item_order_pet.setVisibility(View.VISIBLE);
                //没有分割才添加
                if (rv_item_order_pet.getTag() != "add"){
                    rv_item_order_pet.setTag("add");
                    rv_item_order_pet.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                            super.getItemOffsets(outRect, view, parent, state);
                            int position = parent.getChildAdapterPosition(view);
                            // hide the divider for the last child
                            if (position == 0) {
                            } else {
                                outRect.right = -DensityUtil.dp2px(mContext, 53);
                            }
                        }
                    });
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setReverseLayout(true);//布局反向
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_item_order_pet.setLayoutManager(linearLayoutManager);
                List<String> petImgList = new ArrayList<String>();
                petImgList.clear();
                if (item.petImgList.size() > 2) {
                    petImgList.addAll(item.petImgList.subList(0, 2));
                    tv_item_order_dian.setVisibility(View.VISIBLE);
                } else {
                    petImgList.addAll(item.petImgList);
                    tv_item_order_dian.setVisibility(View.GONE);
                }
                Utils.mLogError("长度："+petImgList.size());
                rv_item_order_pet.setAdapter(new OrderPetAdapter(R.layout.item_order_pet, petImgList));
            } else {
                rv_item_order_pet.setVisibility(View.GONE);
                tv_item_order_dian.setVisibility(View.GONE);
            }
          /*  if (Utils.isStrNull(item.petname))
                tv_orderitem_servicename.setText(item.petname);
            if (Utils.isStrNull(item.customerpetname))
                tv_orderitem_servicename.setText(item.customerpetname);*/
            if (Utils.isStrNull(item.servicename))
                tv_orderitem_servicename.setText(item.servicename);
            tv_orderitem_servicefee.setText("" + item.fee);
            Utils.setText(textView_right_status, item.statusstr, "", View.VISIBLE, View.VISIBLE);
            tv_orderitem_pickup.setVisibility(View.GONE);
            rl_orderitem_jynum.setVisibility(View.GONE);
            tv_orderitem_fwmd.setVisibility(View.GONE);
            if (item.type == 2) {
                rl_orderitem_jynum.setVisibility(View.VISIBLE);
                tv_orderitem_fwmd.setVisibility(View.VISIBLE);
                Utils.setText(tv_orderitem_fwmd, "服务门店：" + item.fosterShopName, "", View.VISIBLE, View.GONE);
                int localdaynum = (int) ((getTimeInMills(item.endtime.split(" ")[0]) - getTimeInMills(item.starttime.split(" ")[0])) / DAYTIMEINMILLS);
                tv_orderitem_totalday.setText("共" + localdaynum + "天");
                Utils.setText(tv_orderitem_starttime, "入住：" + item.starttime.substring(0,item.starttime.length()-3), "入住：", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_orderitem_addrtype, "离店：" + item.endtime.substring(0,item.starttime.length()-3), "离店：", View.VISIBLE, View.VISIBLE);
            } else if (item.type == 3) {
                tv_orderitem_addrtype.setText("服务方式：门店服务");
                String time[] = item.starttime.split(" ");
                String[] timeAOrP = time[1].split(":");
                String AmOrPm = "";
                if (Integer.parseInt(timeAOrP[0]) <= 12) {
                    AmOrPm = "上午";
                } else if (Integer.parseInt(timeAOrP[0]) > 12) {
                    AmOrPm = "下午";
                }
                tv_orderitem_starttime.setText("服务时间：" + time[0] + " " + AmOrPm);
            } else if (item.type == 4) {
                tv_orderitem_starttime.setText("服务时间：" + item.starttime.replace("00", "").replace(":", "").trim());
                tv_orderitem_addrtype.setText("服务方式：门店服务");
            } else if (item.type == 1) {//洗美特
                Utils.setText(tv_orderitem_starttime, "服务时间：" + item.starttime, "服务时间：", View.VISIBLE, View.VISIBLE);
                if (item.addrtype == 1) {
                    tv_orderitem_addrtype.setText("服务方式：到店服务");
                    if (item.pickup == 1) {
                        tv_orderitem_pickup.setVisibility(View.VISIBLE);
                        tv_orderitem_pickup.setText("(需要接送)");
                    } else {
                        tv_orderitem_pickup.setVisibility(View.VISIBLE);
                        tv_orderitem_pickup.setText("(不接送)");
                    }
                } else if (item.addrtype == 2) {
                    tv_orderitem_addrtype.setText("服务方式：上门服务");
                }
            }
            if (item.type == 2) {//寄养
                if (item.hotelStatus == 1 || item.hotelStatus == 21 || item.hotelStatus == 4 || item.hotelStatus == 22) {//待评价,待入住,已入住
                    textView_right_status.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                } else {
                    textView_right_status.setTextColor(mContext.getResources().getColor(R.color.a666666));
                }
            } else {
                if (item.status == 1 || item.status == 4 || item.status == 22) {//待付款,待评价,正在服务
                    textView_right_status.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                } else {
                    textView_right_status.setTextColor(mContext.getResources().getColor(R.color.a666666));
                }
            }
            if (item.isCanGratuity) {
                textview_gratuity.setVisibility(View.VISIBLE);
            } else {
                textview_gratuity.setVisibility(View.GONE);
            }
            if (item.type == 1 && item.status == 2 && item.refType != 4) {//服务单子已付款并且不是寄养订单附带的洗美订单
                textview_appen_single.setVisibility(View.VISIBLE);
                textview_appen_single.setText("升级订单");
            } else {
                textview_appen_single.setVisibility(View.GONE);
            }
            if (item.status == 1 || item.status == 10) {//待支付或者订单升级 去支付
                tv_item_order_time.setVisibility(View.VISIBLE);
                tv_item_order_qxdd.setVisibility(View.VISIBLE);
                tv_item_order_time.updateShow(item.remainTime);
                tv_item_order_time.start(item.remainTime);
            } else {
                tv_item_order_time.setVisibility(View.GONE);
                tv_item_order_qxdd.setVisibility(View.GONE);
            }
            if (item.status == 1) {//待付款
                iv_orderitem_dzf.setVisibility(View.VISIBLE);
            } else {
                iv_orderitem_dzf.setVisibility(View.GONE);
            }
            tv_orderitem_pingjia.setVisibility(View.GONE);
            if (item.status == 4) {//待评价
                tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                tv_orderitem_pingjia.setText(item.evaluate);
            } else {//其他状态
                if (item.type == 1) {//洗美特
                    if (item.status == 2 && item.refType != 4) {//已付款并且不是寄养订单附带的洗美订单
                        tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                        tv_orderitem_pingjia.setText("追加单项");
                        tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                        tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                    } else if (item.status == 10) {//订单升级 去支付
                        tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                        tv_orderitem_pingjia.setText("订单升级-去付款");
                        tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                        tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                    } else if (item.status == 5) {//已关闭(结束)
                        tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                        tv_orderitem_pingjia.setText("再来一单");
                        tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                        tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                    } else if (item.status == 6 || item.status == 7) {//已取消(已支付),到时间自动取消
                        tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                        tv_orderitem_pingjia.setText("重新下单");
                        tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                        tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                    }
                } else if (item.type == 2) {//寄养
                    if (item.hotelStatus == 22) {//有直播
                        tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                        tv_orderitem_pingjia.setText("查看直播");
                        tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.aff3a1e));
                        tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_ff31_border);
                    } else {
                        if (item.status == 5) {//已关闭(结束)
                            tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                            tv_orderitem_pingjia.setText("再来一单");
                            tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                            tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                        } else if (item.status == 6 || item.status == 7) {//已取消(已支付),到时间自动取消
                            tv_orderitem_pingjia.setVisibility(View.VISIBLE);
                            tv_orderitem_pingjia.setText("重新下单");
                            tv_orderitem_pingjia.setTextColor(mContext.getResources().getColor(R.color.a666666));
                            tv_orderitem_pingjia.setBackgroundResource(R.drawable.bg_button_666_border);
                        }
                    }
                }
            }
            //去支付
            iv_orderitem_dzf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.type == 1) {//洗美特
                        Intent intent = new Intent(mContext, WashOrderDetailActivity.class);
                        intent.putExtra("orderid", item.orderid);
                        mContext.startActivity(intent);
                    } else if (item.type == 2) {//寄养
                        Intent intent = new Intent(mContext, FosterOrderDetailNewActivity.class);
                        intent.putExtra("orderid", item.orderid);
                        mContext.startActivity(intent);
                    }
                }
            });
            tv_orderitem_pingjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.status == 4) {//待评价
                        if (item.type == 1) {//洗美特
                            Intent intent = new Intent(mContext, EvaluateNewActivity.class);
                            intent.putExtra("orderid", item.orderid);
                            intent.putExtra("type", item.type);
                            mContext.startActivity(intent);
                        } else if (item.type == 2) {//寄养
                            Intent intent = new Intent(mContext, FosterEvaluteActivity.class);
                            intent.putExtra("orderId", item.orderid);
                            intent.putExtra("shopName", item.fosterShopName);
                            intent.putExtra("shopImg", item.fosterShopImg);
                            mContext.startActivity(intent);
                        }
                    } else {//其他状态
                        if (item.type == 1) {//洗美特
                            if (item.status == 2 && item.refType != 4) {//已付款
                                ToastUtil.showToastShortCenter(mContext, "如需追加单项请到店与美容师沟通");
                                logcountAdd(item.extraLogcountType, item.activityIdExtrax);
                            } else if (item.status == 10) {//订单升级 去支付
                                Intent intent = new Intent(mContext, UpdateOrderConfirmNewActivity.class);
                                intent.putExtra("orderid", item.orderid);
                                mContext.startActivity(intent);
                            } else if (item.status == 5 || item.status == 6 || item.status == 7) {//已关闭(结束),已取消(已支付),到时间自动取消
                                goAppointment(item);
                            }
                        } else if (item.type == 2) {//寄养
                            if (item.hotelStatus == 22) {//有直播
                                if (item.cameraState == 0) {//摄像头状态 0 开启 1 关闭
                                    Intent intent = new Intent(mContext, FosterLiveActivity.class);
                                    intent.putExtra("videoUrl", item.liveUrl);
                                    intent.putExtra("name", item.liveName);
                                    mContext.startActivity(intent);
                                } else {
                                    ToastUtil.showToastShortBottom(mContext, item.liveContent);
                                }
                            } else {
                                if (item.status == 5 || item.status == 6 || item.status == 7) {//已关闭(结束),已取消(已支付),到时间自动取消
                                    for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                                        MApplication.listAppoint.get(i).finish();
                                    }
                                    MApplication.listAppoint.clear();
                                    for (int i = 0; i < MApplication.listAppoint1.size(); i++) {
                                        MApplication.listAppoint1.get(i).finish();
                                    }
                                    MApplication.listAppoint.clear();
                                    MApplication.listAppoint1.clear();
                                    mContext.startActivity(new Intent(mContext, FosterHomeActivity.class));
                                }
                            }
                        }
                    }
                }
            });
            //打赏
            textview_gratuity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item.workerId, item.orderid, item.status);
                }
            });
            //升级订单
            textview_appen_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToastShortCenter(mContext, "如需升级订单请与美容师沟通");
                    logcountAdd(item.updateLogcountType, item.activityIdUpdata);
                }
            });
            ll_item_order_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOrderItemClickListener != null) {
                        onOrderItemClickListener.OnOrderItemClick(helper.getLayoutPosition());
                    }
                }
            });
        }
    }

    public void logcountAdd(String typeid, String activeid) {
        CommUtil.logcountAdd(mActivity, typeid, activeid, statisticsHandler);
    }

    private AsyncHttpResponseHandler statisticsHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void goAppointment(Order item) {
        Intent intent;
        intent = new Intent(mContext, AppointMentActivity.class);
        intent.putExtra("myPets", (Serializable) item.listMyPets);
        intent.putExtra("itemIds", (Serializable) item.itemIds);
        intent.putExtra("commAddr", item.commAddr);
        intent.putExtra("lat", item.commAddr.lat);
        intent.putExtra("lng", item.commAddr.lng);
        intent.putExtra("shop", item.LastShop);
        intent.putExtra("servicetype", item.servicetype);
        intent.putExtra("petAddress", item.commAddr.address);
        intent.putExtra("addressId", item.commAddr.Customer_AddressId);
        intent.putExtra("serviceName", item.servicename);
        intent.putExtra("serviceId", item.serviceid);
        intent.putExtra("serviceLoc", item.serviceLoc);
        mContext.startActivity(intent);
    }

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int workerId, int orderId, int status);
    }

    public OnOrderItemClickListener onOrderItemClickListener;

    public interface OnOrderItemClickListener {
        public void OnOrderItemClick(int position);
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener onOrderItemClickListener) {
        this.onOrderItemClickListener = onOrderItemClickListener;
    }

    @Override
    public long getHeaderId(int position) {
        long headerId = 0;
        if (mData != null && mData.size() > 0 && getItem(position) != null) {
            headerId = getItem(position).headerId;
        }
        return headerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_month_headview, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (mData != null && mData.size() > 0 && mData.size() > position) {
            Order item = mData.get(position);
            if (item != null) {
                Utils.setText(textView, item.yearAndMonth, "", View.VISIBLE, View.GONE);
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private long getTimeInMills(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
