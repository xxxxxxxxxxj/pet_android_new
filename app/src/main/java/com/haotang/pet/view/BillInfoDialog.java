package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.adapter.BillInfoDialogInfoAdapter;
import com.haotang.pet.adapter.BillInfoDialogPayWayAdapter;
import com.haotang.pet.entity.GoodsBill;
import com.haotang.pet.entity.MyBill;
import com.haotang.pet.entity.PayWay;
import com.haotang.pet.util.DensityUtil;
import com.haotang.pet.util.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/27 11:12
 */
public class BillInfoDialog extends Dialog {
    private Context mContext;
    private ImageView iv_billinfo_dialog_close;
    private TextView tv_billinfo_dialog_title;
    private TextView tv_billinfo_dialog_price;
    private TextView tv_billinfo_dialog_desc;
    private RecyclerView rv_billinfo_dialog_payway;
    private TextView tv_billinfo_dialog_tradeno;
    private TextView tv_billinfo_dialog_time;
    private RecyclerView rv_billinfo_dialog_info;
    private MyBill myBill;

    public BillInfoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.billinfo_dialog);
        initEnvironment();
        initControls();
    }

    private void initEnvironment() {
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
    }

    private void setData(MyBill myBill) {
        this.myBill = myBill;
    }

    private void initControls() {
        iv_billinfo_dialog_close = (ImageView) findViewById(R.id.iv_billinfo_dialog_close);
        tv_billinfo_dialog_title = (TextView) findViewById(R.id.tv_billinfo_dialog_title);
        tv_billinfo_dialog_price = (TextView) findViewById(R.id.tv_billinfo_dialog_price);
        tv_billinfo_dialog_desc = (TextView) findViewById(R.id.tv_billinfo_dialog_desc);
        rv_billinfo_dialog_payway = (RecyclerView) findViewById(R.id.rv_billinfo_dialog_payway);
        tv_billinfo_dialog_tradeno = (TextView) findViewById(R.id.tv_billinfo_dialog_tradeno);
        tv_billinfo_dialog_time = (TextView) findViewById(R.id.tv_billinfo_dialog_time);
        rv_billinfo_dialog_info = (RecyclerView) findViewById(R.id.rv_billinfo_dialog_info);
        iv_billinfo_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (myBill != null) {
            Utils.setText(tv_billinfo_dialog_price, myBill.getAmount(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_billinfo_dialog_title, myBill.getDesc(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_billinfo_dialog_desc, myBill.getState(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_billinfo_dialog_tradeno, "交易流水号 " + myBill.getTradeNo(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_billinfo_dialog_time, "交易时间 " + myBill.getTradeDate(), "", View.VISIBLE, View.VISIBLE);
            List<PayWay> payWayList = myBill.getPayWayList();
            List<GoodsBill> goodsList = myBill.getGoodsList();
            if (payWayList != null && payWayList.size() > 0) {
                rv_billinfo_dialog_payway.setHasFixedSize(true);
                rv_billinfo_dialog_payway.setNestedScrollingEnabled(false);
                NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(mContext);
                noScollFullLinearLayoutManager.setScrollEnabled(false);
                rv_billinfo_dialog_payway.setLayoutManager(noScollFullLinearLayoutManager);
                rv_billinfo_dialog_payway.setAdapter(new BillInfoDialogPayWayAdapter(R.layout.item_billinfodialog_payway, payWayList));
            }
            if (goodsList != null && goodsList.size() > 0) {
                rv_billinfo_dialog_info.setLayoutManager(new LinearLayoutManager(mContext));
                rv_billinfo_dialog_info.addItemDecoration(new DividerLinearItemDecoration(mContext, LinearLayoutManager.VERTICAL,
                        DensityUtil.dp2px(mContext, 8),
                        ContextCompat.getColor(mContext, R.color.white)));
                rv_billinfo_dialog_info.setAdapter(new BillInfoDialogInfoAdapter(R.layout.item_billinfodialog_info, goodsList));
            }
        }
    }

    public static class Builder {
        private Context mContext;
        private boolean cancelable = true;
        private MyBill myBill;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public BillInfoDialog build() {
            if (null == mContext)
                return null;
            BillInfoDialog md = new BillInfoDialog(mContext);
            md.setCancelable(cancelable);
            md.setData(myBill);
            return md;
        }

        public Builder setData(MyBill myBill) {
            this.myBill = myBill;
            return this;
        }
    }
}
