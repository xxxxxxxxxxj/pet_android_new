package com.haotang.pet.view;

import android.content.Context;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.util.GlideUtil;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class GlideImageLoaderFourRound extends ImageLoader {
    private int round = 5;

    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(context.getApplicationContext())
//                .load(path).error(R.drawable.icon_production_default)
//                .placeholder(R.drawable.icon_production_default)
//                .into(imageView);
        GlideUtil.loadRoundImg(context.getApplicationContext(), (String) path,imageView, R.drawable.icon_production_default,round);
    }
}