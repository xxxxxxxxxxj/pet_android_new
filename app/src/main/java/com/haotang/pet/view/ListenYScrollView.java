package com.haotang.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author:姜谷蓄
 * @Date:2019/10/22
 * @Description:
 */
public class ListenYScrollView extends ScrollView {

    private Listener listener;

    public ListenYScrollView(Context context) {
        super(context);
    }

    public ListenYScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenYScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null){
            listener.onScroll(t);
        }
    }

    public void setListener(Listener listener){
        this.listener=listener;
    }

    public interface Listener{
        void onScroll(int t);
    }
}
