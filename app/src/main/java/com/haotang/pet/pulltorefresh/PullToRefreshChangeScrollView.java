package com.haotang.pet.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.haotang.pet.R;
import com.haotang.pet.view.ObservablePullScrollView;


/**
 * Created by Administrator on 2018/12/11 0011.
 */

public class PullToRefreshChangeScrollView extends PullToRefreshBase<ObservablePullScrollView> {
    public PullToRefreshChangeScrollView(Context context) {
        super(context);
    }

    public PullToRefreshChangeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshChangeScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshChangeScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);

    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected ObservablePullScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ObservablePullScrollView scrollView = new ObservablePullScrollView(context, attrs);
        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

//    public void firstReFreshing(boolean doScroll) {
//        if (doScroll) {
//            new AsyncTask<Integer, Integer, Integer>() {//该处是针对PullToRefreshScrollView控件的bug进行补充的
//                @SuppressWarnings("ResourceType")
//                @Override
//                protected Integer doInBackground(Integer... params) {
//                    while (true) {
//                        if (mHeaderLayout.getHeight() > 0) {
//                            return null;
//                        }
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//
//                @Override
//                protected void onPostExecute(Integer result) {
//                    Log.e("shiyan", "zhixingle");
//                    setRefreshing(true);
//                }
//            }.execute();
//        } else {
//            setRefreshing(false);
//        }
//    }

    private PullScrollViewListener pullScrollViewListener = null;

    public interface PullScrollViewListener {
        void onScrollChanged(PullToRefreshChangeScrollView pullToRefreshScrollView, int x, int y, int oldX, int oldY);
    }

    public void setPullScrollViewListener(PullScrollViewListener pullScrollViewListener) {
        this.pullScrollViewListener = pullScrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (pullScrollViewListener != null) {
            pullScrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }
}
