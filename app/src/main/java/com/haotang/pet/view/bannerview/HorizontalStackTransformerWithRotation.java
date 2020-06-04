package com.haotang.pet.view.bannerview;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;


public class HorizontalStackTransformerWithRotation implements ViewPager.PageTransformer {
    private static final float CENTER_PAGE_SCALE = 0.85f;
    private int offscreenPageLimit;
    private ViewPager boundViewPager;

    public HorizontalStackTransformerWithRotation(@NonNull ViewPager boundViewPager) {
        this.boundViewPager = boundViewPager;
        this.offscreenPageLimit = boundViewPager.getOffscreenPageLimit();
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pagerWidth = boundViewPager.getWidth();
        float horizontalOffsetBase = (pagerWidth - pagerWidth * CENTER_PAGE_SCALE) / 2 / offscreenPageLimit + DisplayUtil.dp2px(15);

        if (position >= offscreenPageLimit || position <= -1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        if (position >= 0) {
            float translationX = (horizontalOffsetBase - view.getWidth()) * position;
            view.setTranslationX(translationX);
        }
        /*if (position > -1 && position < 0) {
            float rotation = position * 30;
            //view.setRotation(rotation);
            view.setAlpha((position * position * position + 1));
        } else if (position > offscreenPageLimit - 1) {
            view.setAlpha((float) (1 - position + Math.floor(position)));
        } else {
           // view.setRotation(0);
            view.setAlpha(1);
        }*/
        if (position == 0) {
            view.setScaleX(CENTER_PAGE_SCALE);
            view.setScaleY(1f);
        } else {
            float scaleX = Math.min(CENTER_PAGE_SCALE - position * 0.1f, CENTER_PAGE_SCALE);
            float scaleY = Math.min(1f - position * 0.1f, 1f);
            view.setScaleX(scaleX);
            view.setScaleY(scaleY);
        }

        // test code: view初始化时，设置了tag
        String tag = (String) view.getTag();
//        LogUtil.e("viewTag" + tag, "viewTag: " + (String) view.getTag() + " --- transformerPosition: " + position + " --- floor: " + Math.floor(position) + " --- childCount: "+ boundViewPager.getChildCount());
        ViewCompat.setElevation(view, (offscreenPageLimit - position) * 5);
    }
}
