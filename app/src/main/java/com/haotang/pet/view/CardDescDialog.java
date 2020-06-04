package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.haotang.pet.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/22 17:12
 */
public class CardDescDialog extends Dialog {
    public static int DIALOGTYPE_ALERT = 1;
    public static int DIALOGTYPE_CONFIRM = 2;
    private int nDialogType = DIALOGTYPE_ALERT;
    private Context mContext;
    private TextView tvTitle;
    private TextView tvOk;
    private TextView tvMsg;
    private String strTitle;
    private String strOk;
    private CharSequence strMsg;
    private int gravity;
    private android.view.View.OnClickListener positive_listener;
    private android.view.View.OnClickListener default_positive_listener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CardDescDialog.this.dismiss();
            if (null != positive_listener)
                positive_listener.onClick(v);
        }
    };

    public CardDescDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.carddesc_dialog);
        initEnvironment();
        initControls();
    }

    private void initEnvironment() {
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
    }

    private void initControls() {
        tvTitle = (TextView) findViewById(R.id.tv_carddesc_dialog_title);
        tvOk = (TextView) findViewById(R.id.tv_carddesc_dialog_qr);
        tvMsg = (TextView) findViewById(R.id.tv_carddesc_dialog_msg);
        if (null != strTitle)
            tvTitle.setText(strTitle);
        if (null != strOk)
            tvOk.setText(strOk);
        if (null != strMsg)
            tvMsg.setText(strMsg);
        tvMsg.setGravity(gravity);
        tvOk.setOnClickListener(default_positive_listener);
    }

    public void setDialogType(int nDialogType) {
        this.nDialogType = nDialogType;
    }

    public void setTitle(String strtitle) {
        this.strTitle = strtitle;
    }

    public void setOkStr(String strok) {
        this.strOk = strok;
    }

    public void setMsg(CharSequence strMsg) {
        this.strMsg = strMsg;
    }

    public void setPositiveListener(View.OnClickListener positive_listener) {
        this.positive_listener = positive_listener;
    }

    private void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public static class Builder {
        private Context mContext;
        private String strTitle, strOK;
        private CharSequence strMsg;
        private int nDialogType = DIALOGTYPE_ALERT;
        private boolean cancelable = true;
        private View.OnClickListener positive_listener;
        private int gravity;

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

        public Builder setMsg(CharSequence strMsg) {
            this.strMsg = strMsg;
            return this;
        }

        public Builder positiveListener(View.OnClickListener positiveListener) {
            this.positive_listener = positiveListener;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public CardDescDialog build() {
            if (null == mContext)
                return null;
            CardDescDialog md = new CardDescDialog(mContext);
            md.setDialogType(nDialogType);
            md.setTitle(strTitle);
            md.setOkStr(strOK);
            md.setMsg(strMsg);
            md.setGravity(gravity);
            md.setCancelable(cancelable);
            md.setPositiveListener(positive_listener);
            return md;
        }
    }
}
