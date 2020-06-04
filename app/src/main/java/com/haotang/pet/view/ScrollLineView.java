package com.haotang.pet.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.haotang.pet.R;

/**
 * 作者：灼星
 * 时间：2020-04-13
 * 自定义滚动条
 */
public class ScrollLineView extends LinearLayout {
    private View vMainFragLine;
    public ScrollLineView(Context context) {
        super(context);
    }

    public ScrollLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //加载布局
        LayoutInflater.from(getContext()).inflate(R.layout.scroll_line_view,this);
        vMainFragLine = findViewById(R.id.v_mainfrag_line_1);
    }

    /**
     * 获取滚动条宽度
     * @return
     */
    public int getScrollWidth(){
        return  ((ViewGroup) vMainFragLine.getParent()).getWidth() - vMainFragLine.getWidth();
    }

    public View getvMainFragLine() {
        return vMainFragLine;
    }
}
