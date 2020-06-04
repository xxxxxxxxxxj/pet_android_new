package com.haotang.pet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.haotang.pet.R;
import com.haotang.pet.util.MeasureUtil;

/**
 * 说明：可以显示图标和文字的ProgressBar
 * 作者：liuwan
 * 添加时间：2017/3/20 15:49
 */
public class CustomProgressBar extends ProgressBar {

    private Context mContext;
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;
    private float mProgress;
    // IconTextProgressBar的文字大小(sp)
    private static final float TEXT_SIZE_SP = 11f;
    // IconTextProgressBar的图标与文字间距(dp)
    private static final float ICON_TEXT_SPACING_DP = 5f;
    private String[] textArr = new String[]{"2人买", "3人买", "4人买"};

    public void setTextArr(String[] textArr) {
        this.textArr = textArr;
        //绘制
        invalidate();
    }

    public String[] getTextArr() {
        return textArr;
    }

    public CustomProgressBar(Context context) {
        super(context, null, android.R.attr.progressBarStyleHorizontal);
        mContext = context;
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 设置下载进度
     */
    public synchronized void setProgress(float progress) {
        super.setProgress((int) progress);
        mProgress = progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.a333333));
        for (int i = 0; i < textArr.length; i++) {
            Rect textRect = new Rect();
            mPaint.getTextBounds(textArr[i], 0, textArr[i].length(), textRect);
            // 仅绘制文字
            float textX = (getWidth() / textArr.length * (i + 1) / 2) - textRect.centerX() + (getWidth() / textArr.length / 2 * i);
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(textArr[i], textX, textY, mPaint);
        }
    }

    private void init() {
        setIndeterminate(false);
        setIndeterminateDrawable(ContextCompat.getDrawable(mContext,
                android.R.drawable.progress_indeterminate_horizontal));
        setProgressDrawable(ContextCompat.getDrawable(mContext,
                R.drawable.pb_shape_blue));
        setMax(100);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(MeasureUtil.sp2px(mContext, TEXT_SIZE_SP));
        mPaint.setTypeface(Typeface.MONOSPACE);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }
}
