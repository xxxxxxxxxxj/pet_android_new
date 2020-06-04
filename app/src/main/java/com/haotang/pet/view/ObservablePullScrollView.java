package com.haotang.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/12/11 0011.
 */

public class ObservablePullScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public ObservablePullScrollView(Context context) {
        super(context);
    }

    public ObservablePullScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservablePullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(ObservablePullScrollView scrollView, int x, int y, int oldX, int oldY);

    }

}
