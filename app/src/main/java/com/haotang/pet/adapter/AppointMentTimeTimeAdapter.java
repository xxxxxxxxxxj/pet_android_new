package com.haotang.pet.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.pet.R;
import com.haotang.pet.entity.AppointMentDate;
import com.haotang.pet.entity.AppointMentTime;
import com.haotang.pet.entity.AppointMentZhengDianTime;
import com.haotang.pet.entity.AppointZDTimeEvent;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.NoScollFullGridLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/8/25 10:04
 */
public class AppointMentTimeTimeAdapter extends BaseQuickAdapter<AppointMentZhengDianTime, BaseViewHolder> {
    public boolean isChangeOrder;

    public AppointMentTimeTimeAdapter(int layoutResId, List<AppointMentZhengDianTime> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AppointMentZhengDianTime item) {
        LinearLayout ll_appointtime_time1 = helper.getView(R.id.ll_appointtime_time1);
        RelativeLayout rl_appointtime_time1 = helper.getView(R.id.rl_appointtime_time1);
        TextView tv_appointtime_time1 = helper.getView(R.id.tv_appointtime_time1);
        ImageView iv_appointtime_time1 = helper.getView(R.id.iv_appointtime_time1);
        ImageView iv_appointtime_time1_jianer = helper.getView(R.id.iv_appointtime_time1_jianer);
        ImageView iv_appointtime_time1_js = helper.getView(R.id.iv_appointtime_time1_js);

        LinearLayout ll_appointtime_time2 = helper.getView(R.id.ll_appointtime_time2);
        RelativeLayout rl_appointtime_time2 = helper.getView(R.id.rl_appointtime_time2);
        TextView tv_appointtime_time2 = helper.getView(R.id.tv_appointtime_time2);
        ImageView iv_appointtime_time2 = helper.getView(R.id.iv_appointtime_time2);
        ImageView iv_appointtime_time2_jianer = helper.getView(R.id.iv_appointtime_time2_jianer);
        ImageView iv_appointtime_time2_js = helper.getView(R.id.iv_appointtime_time2_js);

        LinearLayout ll_appointtime_time3 = helper.getView(R.id.ll_appointtime_time3);
        RelativeLayout rl_appointtime_time3 = helper.getView(R.id.rl_appointtime_time3);
        TextView tv_appointtime_time3 = helper.getView(R.id.tv_appointtime_time3);
        ImageView iv_appointtime_time3 = helper.getView(R.id.iv_appointtime_time3);
        ImageView iv_appointtime_time3_jianer = helper.getView(R.id.iv_appointtime_time3_jianer);
        ImageView iv_appointtime_time3_js = helper.getView(R.id.iv_appointtime_time3_js);

        LinearLayout ll_appointtime_time4 = helper.getView(R.id.ll_appointtime_time4);
        RelativeLayout rl_appointtime_time4 = helper.getView(R.id.rl_appointtime_time4);
        TextView tv_appointtime_time4 = helper.getView(R.id.tv_appointtime_time4);
        ImageView iv_appointtime_time4 = helper.getView(R.id.iv_appointtime_time4);
        ImageView iv_appointtime_time4_jianer = helper.getView(R.id.iv_appointtime_time4_jianer);
        RecyclerView rv_appointtime_time = helper.getView(R.id.rv_appointtime_time);
        LinearLayout ll_item_appointtime_jianer = helper.getView(R.id.ll_item_appointtime_jianer);
        ImageView iv_appointtime_time4_js = helper.getView(R.id.iv_appointtime_time4_js);
        rv_appointtime_time.setVisibility(View.GONE);
        if (item != null) {
            final AppointMentDate appointMentDate1 = item.getAppointMentDate1();
            final AppointMentDate appointMentDate2 = item.getAppointMentDate2();
            final AppointMentDate appointMentDate3 = item.getAppointMentDate3();
            final AppointMentDate appointMentDate4 = item.getAppointMentDate4();
            boolean select1 = item.isSelect1();
            boolean select2 = item.isSelect2();
            boolean select3 = item.isSelect3();
            boolean select4 = item.isSelect4();
            if (appointMentDate1 != null) {
                ll_appointtime_time1.setVisibility(View.VISIBLE);
                Utils.setText(tv_appointtime_time1, appointMentDate1.getTime(), "", View.VISIBLE, View.VISIBLE);
                int isFull = appointMentDate1.getIsFull();
                List<AppointMentTime> times = appointMentDate1.getTimes();
                if (isFull == 0) {
                    tv_appointtime_time1.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    iv_appointtime_time1.setVisibility(View.VISIBLE);
                    if (select1) {
                        if (times != null && times.size() > 0) {
                            if (appointMentDate1.getPickup() == 1) {//可接送
                                iv_appointtime_time1_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time1_js.setVisibility(View.GONE);
                            }
                            ll_item_appointtime_jianer.setVisibility(View.VISIBLE);
                            ll_item_appointtime_jianer.bringToFront();
                            iv_appointtime_time1.setImageResource(R.drawable.icon_time_up);
                            rl_appointtime_time1.setBackgroundResource(R.drawable.bg_fff4f3_ffbdb7_border);
                            iv_appointtime_time1_jianer.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setHasFixedSize(true);
                            rv_appointtime_time.setNestedScrollingEnabled(false);
                            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                                    NoScollFullGridLayoutManager(rv_appointtime_time, mContext, 3, GridLayoutManager.VERTICAL, false);
                            noScollFullGridLayoutManager.setScrollEnabled(false);
                            rv_appointtime_time.setLayoutManager(noScollFullGridLayoutManager);
                            rv_appointtime_time.setAdapter(new AppointMentTimeAdapter(R.layout.item_time_time, times, isChangeOrder));
                        } else {
                            if (appointMentDate1.getPickup() == 1) {//可接送
                                iv_appointtime_time1_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time1_js.setVisibility(View.GONE);
                            }
                            iv_appointtime_time1.setImageResource(R.drawable.icon_time_down);
                            rl_appointtime_time1.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                            iv_appointtime_time1_jianer.setVisibility(View.GONE);
                        }
                    } else {
                        if (appointMentDate1.getPickup() == 1) {//可接送
                            iv_appointtime_time1_js.setVisibility(View.VISIBLE);
                        } else {//不可接送
                            iv_appointtime_time1_js.setVisibility(View.GONE);
                        }
                        rl_appointtime_time1.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        iv_appointtime_time1.setImageResource(R.drawable.icon_time_down);
                        iv_appointtime_time1_jianer.setVisibility(View.GONE);
                    }
                } else {
                    iv_appointtime_time1_js.setVisibility(View.GONE);
                    tv_appointtime_time1.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    rl_appointtime_time1.setBackgroundResource(R.drawable.bg_trans_bbbb_border);
                    iv_appointtime_time1.setVisibility(View.GONE);
                    iv_appointtime_time1_jianer.setVisibility(View.GONE);
                }
            } else {
                iv_appointtime_time1_js.setVisibility(View.GONE);
                ll_appointtime_time1.setVisibility(View.INVISIBLE);
                iv_appointtime_time1_jianer.setVisibility(View.GONE);
            }

            if (appointMentDate2 != null) {
                ll_appointtime_time2.setVisibility(View.VISIBLE);
                Utils.setText(tv_appointtime_time2, appointMentDate2.getTime(), "", View.VISIBLE, View.VISIBLE);
                int isFull = appointMentDate2.getIsFull();
                List<AppointMentTime> times = appointMentDate2.getTimes();
                if (isFull == 0) {
                    tv_appointtime_time2.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    iv_appointtime_time2.setVisibility(View.VISIBLE);
                    if (select2) {
                        if (times != null && times.size() > 0) {
                            if (appointMentDate2.getPickup() == 1) {//可接送
                                iv_appointtime_time2_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time2_js.setVisibility(View.GONE);
                            }
                            ll_item_appointtime_jianer.setVisibility(View.VISIBLE);
                            ll_item_appointtime_jianer.bringToFront();
                            iv_appointtime_time2.setImageResource(R.drawable.icon_time_up);
                            rl_appointtime_time2.setBackgroundResource(R.drawable.bg_fff4f3_ffbdb7_border);
                            iv_appointtime_time2_jianer.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setHasFixedSize(true);
                            rv_appointtime_time.setNestedScrollingEnabled(false);
                            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                                    NoScollFullGridLayoutManager(rv_appointtime_time, mContext, 3, GridLayoutManager.VERTICAL, false);
                            noScollFullGridLayoutManager.setScrollEnabled(false);
                            rv_appointtime_time.setLayoutManager(noScollFullGridLayoutManager);
                            rv_appointtime_time.setAdapter(new AppointMentTimeAdapter(R.layout.item_time_time, times, isChangeOrder));
                        } else {
                            if (appointMentDate2.getPickup() == 1) {//可接送
                                iv_appointtime_time2_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time2_js.setVisibility(View.GONE);
                            }
                            iv_appointtime_time2.setImageResource(R.drawable.icon_time_down);
                            rl_appointtime_time2.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                            iv_appointtime_time2_jianer.setVisibility(View.GONE);
                        }
                    } else {
                        if (appointMentDate2.getPickup() == 1) {//可接送
                            iv_appointtime_time2_js.setVisibility(View.VISIBLE);
                        } else {//不可接送
                            iv_appointtime_time2_js.setVisibility(View.GONE);
                        }
                        rl_appointtime_time2.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        iv_appointtime_time2.setImageResource(R.drawable.icon_time_down);
                        iv_appointtime_time2_jianer.setVisibility(View.GONE);
                    }
                } else {
                    iv_appointtime_time2_js.setVisibility(View.GONE);
                    tv_appointtime_time2.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    rl_appointtime_time2.setBackgroundResource(R.drawable.bg_trans_bbbb_border);
                    iv_appointtime_time2.setVisibility(View.GONE);
                    iv_appointtime_time2_jianer.setVisibility(View.GONE);
                }
            } else {
                iv_appointtime_time2_js.setVisibility(View.GONE);
                ll_appointtime_time2.setVisibility(View.INVISIBLE);
                iv_appointtime_time2_jianer.setVisibility(View.GONE);
            }

            if (appointMentDate3 != null) {
                ll_appointtime_time3.setVisibility(View.VISIBLE);
                Utils.setText(tv_appointtime_time3, appointMentDate3.getTime(), "", View.VISIBLE, View.VISIBLE);
                int isFull = appointMentDate3.getIsFull();
                List<AppointMentTime> times = appointMentDate3.getTimes();
                if (isFull == 0) {
                    tv_appointtime_time3.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    iv_appointtime_time3.setVisibility(View.VISIBLE);
                    if (select3) {
                        if (times != null && times.size() > 0) {
                            if (appointMentDate3.getPickup() == 1) {//可接送
                                iv_appointtime_time3_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time3_js.setVisibility(View.GONE);
                            }
                            ll_item_appointtime_jianer.setVisibility(View.VISIBLE);
                            ll_item_appointtime_jianer.bringToFront();
                            iv_appointtime_time3.setImageResource(R.drawable.icon_time_up);
                            rl_appointtime_time3.setBackgroundResource(R.drawable.bg_fff4f3_ffbdb7_border);
                            iv_appointtime_time3_jianer.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setHasFixedSize(true);
                            rv_appointtime_time.setNestedScrollingEnabled(false);
                            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                                    NoScollFullGridLayoutManager(rv_appointtime_time, mContext, 3, GridLayoutManager.VERTICAL, false);
                            noScollFullGridLayoutManager.setScrollEnabled(false);
                            rv_appointtime_time.setLayoutManager(noScollFullGridLayoutManager);
                            rv_appointtime_time.setAdapter(new AppointMentTimeAdapter(R.layout.item_time_time, times, isChangeOrder));
                        } else {
                            if (appointMentDate3.getPickup() == 1) {//可接送
                                iv_appointtime_time3_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time3_js.setVisibility(View.GONE);
                            }
                            iv_appointtime_time3.setImageResource(R.drawable.icon_time_down);
                            rl_appointtime_time3.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                            iv_appointtime_time3_jianer.setVisibility(View.GONE);
                        }
                    } else {
                        if (appointMentDate3.getPickup() == 1) {//可接送
                            iv_appointtime_time3_js.setVisibility(View.VISIBLE);
                        } else {//不可接送
                            iv_appointtime_time3_js.setVisibility(View.GONE);
                        }
                        rl_appointtime_time3.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        iv_appointtime_time3.setImageResource(R.drawable.icon_time_down);
                        iv_appointtime_time3_jianer.setVisibility(View.GONE);
                    }
                } else {
                    iv_appointtime_time3_js.setVisibility(View.GONE);
                    tv_appointtime_time3.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    rl_appointtime_time3.setBackgroundResource(R.drawable.bg_trans_bbbb_border);
                    iv_appointtime_time3.setVisibility(View.GONE);
                    iv_appointtime_time3_jianer.setVisibility(View.GONE);
                }
            } else {
                iv_appointtime_time3_js.setVisibility(View.GONE);
                ll_appointtime_time3.setVisibility(View.INVISIBLE);
                iv_appointtime_time3_jianer.setVisibility(View.GONE);
            }

            if (appointMentDate4 != null) {
                ll_appointtime_time4.setVisibility(View.VISIBLE);
                Utils.setText(tv_appointtime_time4, appointMentDate4.getTime(), "", View.VISIBLE, View.VISIBLE);
                int isFull = appointMentDate4.getIsFull();
                List<AppointMentTime> times = appointMentDate4.getTimes();
                if (isFull == 0) {
                    tv_appointtime_time4.setTextColor(mContext.getResources().getColor(R.color.aD0021B));
                    iv_appointtime_time4.setVisibility(View.VISIBLE);
                    if (select4) {
                        if (times != null && times.size() > 0) {
                            if (appointMentDate4.getPickup() == 1) {//可接送
                                iv_appointtime_time4_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time4_js.setVisibility(View.GONE);
                            }
                            ll_item_appointtime_jianer.setVisibility(View.VISIBLE);
                            ll_item_appointtime_jianer.bringToFront();
                            iv_appointtime_time4.setImageResource(R.drawable.icon_time_up);
                            rl_appointtime_time4.setBackgroundResource(R.drawable.bg_fff4f3_ffbdb7_border);
                            iv_appointtime_time4_jianer.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setVisibility(View.VISIBLE);
                            rv_appointtime_time.setHasFixedSize(true);
                            rv_appointtime_time.setNestedScrollingEnabled(false);
                            NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                                    NoScollFullGridLayoutManager(rv_appointtime_time, mContext, 3, GridLayoutManager.VERTICAL, false);
                            noScollFullGridLayoutManager.setScrollEnabled(false);
                            rv_appointtime_time.setLayoutManager(noScollFullGridLayoutManager);
                            rv_appointtime_time.setAdapter(new AppointMentTimeAdapter(R.layout.item_time_time, times, isChangeOrder));
                        } else {
                            if (appointMentDate4.getPickup() == 1) {//可接送
                                iv_appointtime_time4_js.setVisibility(View.VISIBLE);
                            } else {//不可接送
                                iv_appointtime_time4_js.setVisibility(View.GONE);
                            }
                            iv_appointtime_time4.setImageResource(R.drawable.icon_time_down);
                            rl_appointtime_time4.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                            iv_appointtime_time4_jianer.setVisibility(View.GONE);
                        }
                    } else {
                        if (appointMentDate4.getPickup() == 1) {//可接送
                            iv_appointtime_time4_js.setVisibility(View.VISIBLE);
                        } else {//不可接送
                            iv_appointtime_time4_js.setVisibility(View.GONE);
                        }
                        rl_appointtime_time4.setBackgroundResource(R.drawable.bg_ffbdb7_border);
                        iv_appointtime_time4.setImageResource(R.drawable.icon_time_down);
                        iv_appointtime_time4_jianer.setVisibility(View.GONE);
                    }
                } else {
                    iv_appointtime_time4_js.setVisibility(View.GONE);
                    tv_appointtime_time4.setTextColor(mContext.getResources().getColor(R.color.aBBBBBB));
                    rl_appointtime_time4.setBackgroundResource(R.drawable.bg_trans_bbbb_border);
                    iv_appointtime_time4.setVisibility(View.GONE);
                    iv_appointtime_time4_jianer.setVisibility(View.GONE);
                }
            } else {
                iv_appointtime_time4_js.setVisibility(View.GONE);
                ll_appointtime_time4.setVisibility(View.INVISIBLE);
                iv_appointtime_time4_jianer.setVisibility(View.GONE);
            }

            rl_appointtime_time1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointMentDate1 != null && appointMentDate1.getIsFull() == 0 && appointMentDate1.getTimes() != null && appointMentDate1.getTimes().size() > 0) {
                        EventBus.getDefault().post(new AppointZDTimeEvent(1, helper.getLayoutPosition()));
                    }
                }
            });
            rl_appointtime_time2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointMentDate2 != null && appointMentDate2.getIsFull() == 0 && appointMentDate2.getTimes() != null && appointMentDate2.getTimes().size() > 0) {
                        EventBus.getDefault().post(new AppointZDTimeEvent(2, helper.getLayoutPosition()));
                    }
                }
            });
            rl_appointtime_time3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointMentDate3 != null && appointMentDate3.getIsFull() == 0 && appointMentDate3.getTimes() != null && appointMentDate3.getTimes().size() > 0) {
                        EventBus.getDefault().post(new AppointZDTimeEvent(3, helper.getLayoutPosition()));
                    }
                }
            });
            rl_appointtime_time4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appointMentDate4 != null && appointMentDate4.getIsFull() == 0 && appointMentDate4.getTimes() != null && appointMentDate4.getTimes().size() > 0) {
                        EventBus.getDefault().post(new AppointZDTimeEvent(4, helper.getLayoutPosition()));
                    }
                }
            });
        }
    }

    public void setChangeOrder(boolean isChangeOrder) {
        this.isChangeOrder = isChangeOrder;
    }

}
