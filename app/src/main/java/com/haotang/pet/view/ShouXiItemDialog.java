package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.adapter.ShouXiItemAdapter;
import com.haotang.pet.entity.ApointMentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/13 16:06
 */
public class ShouXiItemDialog extends Dialog {
    public static int DIALOGTYPE_ALERT = 1;
    public static int DIALOGTYPE_CONFIRM = 2;

    private int nDialogType = DIALOGTYPE_ALERT;
    private Context mContext;
    private TextView tvTitle;
    private TextView tvOk;
    private String strTitle;
    private RecyclerView rv_shouxiitem_dialog;
    private String strOk;
    private List<ApointMentItem> itemList = new ArrayList<ApointMentItem>();
    private android.view.View.OnClickListener positive_listener;
    private ShouXiItemAdapter shouXiItemAdapter;
    private android.view.View.OnClickListener default_positive_listener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ShouXiItemDialog.this.dismiss();
            if (null != positive_listener)
                positive_listener.onClick(v);
        }
    };

    public ShouXiItemDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shouxiitem_dialog);
        initEnvironment();
        initControls();
    }

    private void initEnvironment() {
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
    }

    private void initControls() {
        tvTitle = (TextView) findViewById(R.id.tv_shouxiitem_dialog_title);
        tvOk = (TextView) findViewById(R.id.tv_shouxiitem_dialog_cancel);
        rv_shouxiitem_dialog = (RecyclerView) findViewById(R.id.rv_shouxiitem_dialog);
        if (null != strTitle)
            tvTitle.setText(strTitle);
        tvOk.setOnClickListener(default_positive_listener);
        if (null != strOk)
            tvOk.setText(strOk);
        rv_shouxiitem_dialog.setHasFixedSize(true);
        rv_shouxiitem_dialog.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                NoScollFullGridLayoutManager(rv_shouxiitem_dialog, mContext, 3, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        rv_shouxiitem_dialog.setLayoutManager(noScollFullGridLayoutManager);
        shouXiItemAdapter = new ShouXiItemAdapter(R.layout.item_shouxiitem_dialog, itemList);
        rv_shouxiitem_dialog.setAdapter(shouXiItemAdapter);
    }

    public void setDialogType(int nDialogType) {
        this.nDialogType = nDialogType;
    }

    private void setData(List<ApointMentItem> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    public void setTitle(String strtitle) {
        this.strTitle = strtitle;
    }

    public void setOkStr(String strok) {
        this.strOk = strok;
    }

    public void setPositiveListener(View.OnClickListener positive_listener) {
        this.positive_listener = positive_listener;
    }

    public static class Builder {
        private Context mContext;
        private String strTitle, strOK;
        private List<ApointMentItem> itemList = new ArrayList<ApointMentItem>();
        private int nDialogType = DIALOGTYPE_ALERT;
        private boolean cancelable = true;
        private View.OnClickListener positive_listener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String title) {
            this.strTitle = title;
            return this;
        }

        public Builder setType(int nType) {
            this.nDialogType = nType;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOKStr(String strok) {
            this.strOK = strok;
            return this;
        }

        public Builder positiveListener(View.OnClickListener positiveListener) {
            this.positive_listener = positiveListener;
            return this;
        }

        public ShouXiItemDialog build() {
            if (null == mContext)
                return null;
            ShouXiItemDialog md = new ShouXiItemDialog(mContext);
            md.setDialogType(nDialogType);
            md.setTitle(strTitle);
            md.setOkStr(strOK);
            md.setData(itemList);
            md.setCancelable(cancelable);
            md.setPositiveListener(positive_listener);
            return md;
        }

        public Builder setData(List<ApointMentItem> itemList) {
            this.itemList.clear();
            this.itemList.addAll(itemList);
            return this;
        }
    }
}
