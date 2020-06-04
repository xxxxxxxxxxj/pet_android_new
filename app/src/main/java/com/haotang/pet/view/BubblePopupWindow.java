package com.haotang.pet.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

/**
 * @author yuyh.
 * @date 2016/8/25.
 */
public class BubblePopupWindow extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private View parentView;
    private TextView tv_mainfrag_pop;
    private BubbleRelativeLayout bubbleView;
    private Context context;

    public BubblePopupWindow(Context context, String str) {
        this.context = context;
        setBubbleView();
        setPopConfig();
        initData(context, str);
    }

    /**
     * 初始化数据
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/29,22:03
     * <h3>UpdateTime</h3> 2016/6/29,22:03
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param context
     * @param str
     */
    private void initData(Context context, String str) {
        //给tv_mainfrag_pop添加数据
        Utils.setText(tv_mainfrag_pop, str, "", View.VISIBLE, View.INVISIBLE);
    }

    /**
     * 配置弹出框属性
     *
     * @version 1.0
     * @createTime 2015/12/1,12:45
     * @updateTime 2015/12/1,12:45
     * @createAuthor
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setPopConfig() {
        setClippingEnabled(false);
        this.setAnimationStyle(R.style.popwin_anim_style);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);// 设置外部触摸会关闭窗口
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }

    public TextView getTextView() {
        return tv_mainfrag_pop;
    }

    public void setBubbleView() {
        parentView = View.inflate(context, R.layout.mainfrag_pop, null);
        tv_mainfrag_pop = (TextView) parentView.findViewById(R.id.tv_mainfrag_pop);
        bubbleView = new BubbleRelativeLayout(context);
        bubbleView.setBackgroundColor(Color.TRANSPARENT);
        bubbleView.addView(parentView);
        setContentView(bubbleView);
    }

    public void setParam(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void show(View parent) {
        show(parent, Gravity.TOP, getMeasuredWidth() / 2);
    }

    public void show(View parent, int gravity) {
        show(parent, gravity, getMeasuredWidth() / 2);
    }

    /**
     * 显示弹窗
     *
     * @param parent
     * @param gravity
     * @param bubbleOffset 气泡尖角位置偏移量。默认位于中间
     */
    public void show(View parent, int gravity, float bubbleOffset) {
        BubbleRelativeLayout.BubbleLegOrientation orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
        if (!this.isShowing()) {
            switch (gravity) {
                case Gravity.BOTTOM:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    break;
                case Gravity.TOP:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.BOTTOM;
                    break;
                case Gravity.RIGHT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
                    break;
                case Gravity.LEFT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.RIGHT;
                    break;
                default:
                    break;
            }
            bubbleView.setBubbleParams(orientation, bubbleOffset); // 设置气泡布局方向及尖角偏移

            int[] location = new int[2];
            parent.getLocationOnScreen(location);

            switch (gravity) {
                case Gravity.BOTTOM:
                    showAsDropDown(parent);
                    break;
                case Gravity.TOP:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] - getMeasureHeight());
                    break;
                case Gravity.RIGHT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1] - (parent.getHeight() / 2));
                    break;
                case Gravity.LEFT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - getMeasuredWidth(), location[1] - (parent.getHeight() / 2));
                    break;
                default:
                    break;
            }
        } else {
            this.dismiss();
        }
    }

    /**
     * 测量高度
     * @return
     */
    public int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    /**
     * 测量宽度
     * @return
     */
    public int getMeasuredWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return popWidth;
    }
}
