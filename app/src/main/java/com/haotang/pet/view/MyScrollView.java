package com.haotang.pet.view;

import android.content.Context;

import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.haotang.pet.util.Utils;

import java.util.ArrayList;

/**
 * @author:姜谷蓄
 * @Date:2019/10/22
 * @Description:
 */
public class MyScrollView extends NestedScrollView {

    private Listener listener;
    private int mLastMotionY;
    private View view;
    private ArrayList<View> arrayList = new ArrayList<>();

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScroll(t);
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    public void addRecyclerView(View view){
        if(!arrayList.contains(view))
            arrayList.add(view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(y - mLastMotionY);
                //点击事件落在View上就不在拦截
                if (inRangeChild(ev) && dx > ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    return false;
                }
                Utils.mLogError("MyScrollview onInterceptTouchEvent "+dx);

                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否点击在子特定View里面
     */
    private boolean inRangeChild( MotionEvent ev){
        for(View view : arrayList){
            if (inRangeOfView(view,ev))
                return true;
        }

        return false;
    }

    private boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onScroll(int t);
    }
}
