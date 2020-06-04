package com.haotang.pet.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haotang.pet.util.ScreenUtil;

/**
 * Created by KID on 2017/11/14.
 * 随意拖动的view
 */

@SuppressLint("AppCompatCustomView")
public class DragView extends ImageView {
    private final Context context;
    private int imageWidth;
    private int imageHeight;
    //是否拖动
    private boolean isDrag = false;
    private float downX;
    private float downY;
    private int size = FrameLayout.LayoutParams.WRAP_CONTENT;
    private boolean isHaveStatusBar;
    private boolean scaleToWidth = false; // this flag determines if should
    private int screenWidth;
    private int screenHeight;

    public boolean isHaveStatusBar() {
        return isHaveStatusBar;
    }

    public void setHaveStatusBar(boolean haveStatusBar) {
        isHaveStatusBar = haveStatusBar;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDrag() {
        return isDrag;
    }

    public DragView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        this.setScaleType(ScaleType.FIT_XY);
        screenWidth = ScreenUtil.getScreenWidth(context);
        screenHeight = ScreenUtil.getScreenHeight(context) - getStatusBarHeight();
    }

    public void setImageWidth(int w) {
        imageWidth = w;
    }

    public void setImageHeight(int h) {
        imageHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * if both width and height are set scale width first. modify in future
         * if necessary
         */

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            scaleToWidth = true;
        } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            scaleToWidth = false;
        } else
            throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

        if (imageWidth == 0) {
            // nothing to measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
            if (scaleToWidth) {
                Log.e("TAG", "scaleToWidth = " + scaleToWidth);
                Log.e("TAG", "imageWidth = " + imageWidth);
                Log.e("TAG", "imageHeight = " + imageHeight);
                Log.e("TAG", "widthMode = " + widthMode);
                Log.e("TAG", "heightMode = " + heightMode);
                Log.e("TAG", "width = " + width);
                Log.e("TAG", "height = " + height);
                Log.e("TAG", "screenWidth = " + screenWidth);
                Log.e("TAG", "screenHeight = " + screenHeight);
                int iw = imageWidth;
                int ih = imageHeight;
                int heightC = width * ih / iw;
                if (height > 0)
                    if (heightC > height) {
                        // dont let hegiht be greater then set max
                        heightC = height;
                        width = heightC * iw / ih;
                    }

                this.setScaleType(ScaleType.FIT_XY);
                setMeasuredDimension(imageWidth, imageHeight);
            } else {
                Log.e("TAG", "scaleToWidth = " + scaleToWidth);
                Log.e("TAG", "imageWidth = " + imageWidth);
                Log.e("TAG", "imageHeight = " + imageHeight);
                Log.e("TAG", "widthMode = " + widthMode);
                Log.e("TAG", "heightMode = " + heightMode);
                Log.e("TAG", "width = " + width);
                Log.e("TAG", "height = " + height);
                Log.e("TAG", "screenWidth = " + screenWidth);
                Log.e("TAG", "screenHeight = " + screenHeight);
                // need to scale to height instead
                int marg = 0;
                if (getParent() != null) {
                    if (getParent().getParent() != null) {
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }
                int iw = imageWidth;
                int ih = imageHeight / 2;
                width = height * iw / ih;
                height -= marg;
                setMeasuredDimension(imageWidth, imageHeight);
            }
        }
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrag = false;
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("kid", "ACTION_MOVE");
                    final float xDistance = event.getX() - downX;
                    final float yDistance = event.getY() - downY;
                    int l, r, t, b;
                    //当水平或者垂直滑动距离大于10,才算拖动事件
                    if (Math.abs(xDistance) > 10 || Math.abs(yDistance) > 10) {
                        Log.e("kid", "Drag");
                        isDrag = true;
                        l = (int) (getLeft() + xDistance);
                        r = l + imageWidth;
                        t = (int) (getTop() + yDistance);
                        b = t + imageHeight;
                        //不划出边界判断,此处应按照项目实际情况,因为本项目需求移动的位置是手机全屏,
                        // 所以才能这么写,如果是固定区域,要得到父控件的宽高位置后再做处理
                        if (l < 0) {
                            l = 0;
                            r = l + imageWidth;
                        } else if (r > screenWidth) {
                            r = screenWidth;
                            l = r - imageWidth;
                        }
                        if (isHaveStatusBar) {
                            if (t < getStatusBarHeight()) {
                                t = getStatusBarHeight();
                                b = t + imageHeight;
                            } else if (b > (screenHeight + getStatusBarHeight())) {
                                b = (screenHeight + getStatusBarHeight());
                                t = b - imageHeight;
                            }
                        } else {
                            if (t < 0) {
                                t = 0;
                                b = t + imageHeight;
                            } else if (b > screenHeight) {
                                b = screenHeight;
                                t = b - imageHeight;
                            }
                        }
                        this.layout(l, t, r, b);
                        this.setLayoutParams(createLayoutParams(l, t, 0, 0));
                        Log.e("kid", "l = " + l + ",t = " + t + ",r = " + r + ",b = " + b);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("kid", "ACTION_UP");
                    this.setLayoutParams(createLayoutParams(getLeft(), getTop(), 0, 0));
                    Log.e("kid", "getLeft() = " + getLeft() + ",getTop() = " + getTop() + ",getRight() = " + getRight() + ",getBottom() = " + getBottom());
                    setPressed(false);
                    if (onParamsListener != null) {
                        onParamsListener.OnParams(getLeft(), getTop(), getRight(), getBottom());
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("kid", "ACTION_CANCEL");
                    Log.e("kid", "getLeft() = " + getLeft() + ",getTop() = " + getTop() + ",getRight() = " + getRight() + ",getBottom() = " + getBottom());
                    this.setLayoutParams(createLayoutParams(getLeft(), getTop(), 0, 0));
                    setPressed(false);
                    if (onParamsListener != null) {
                        onParamsListener.OnParams(getLeft(), getTop(), getRight(), getBottom());
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    public RelativeLayout.LayoutParams createLayoutParams(int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;
    }

    public OnParamsListener onParamsListener = null;

    public interface OnParamsListener {
        public void OnParams(int left, int top, int right, int bottom);
    }

    public void setOnParamsListener(OnParamsListener onParamsListener) {
        this.onParamsListener = onParamsListener;
    }
}
