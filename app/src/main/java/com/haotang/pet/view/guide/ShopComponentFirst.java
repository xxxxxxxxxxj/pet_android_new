package com.haotang.pet.view.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.binioter.guideview.Component;
import com.haotang.pet.R;

public class ShopComponentFirst implements Component {
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_shop_first,null);

        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 6;
    }
}