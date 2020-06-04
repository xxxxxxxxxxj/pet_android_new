package com.haotang.pet.view;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author:姜谷蓄
 * @Date:2020/2/28
 * @Description:
 */
public class WrapRecyLinearLayoutManger extends LinearLayoutManager {
    public WrapRecyLinearLayoutManger(Context context) {
        super( context );
    }

    public WrapRecyLinearLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super( context, orientation, reverseLayout );
    }

    public WrapRecyLinearLayoutManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //这里捕获之前的数组越界问题...
            super.onLayoutChildren( recycler, state );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }
}
