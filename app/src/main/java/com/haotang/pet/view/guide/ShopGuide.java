package com.haotang.pet.view.guide;

import android.app.Activity;
import android.view.View;

import com.binioter.guideview.Component;
import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;
import com.haotang.pet.R;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

/**
 * 门店遮盖层
 */
public class ShopGuide {

    public interface ShopGuideInterface{
        void onShopGuideDismiss();
    }

    /**
     * 门店选择提示
     * @param activity
     * @param target
     * @param target2
     */
    public void showGuideView(final Activity activity, final View target, final View target2, final ShopGuideInterface shopGuideInterface){
        if (SharedPreferenceUtil.getInstance(activity).getIsShopGuideFirst()){
            //设置白色背景
            target.setBackgroundResource(R.drawable.bg_white_round15);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(target)
                    .setAlpha(128)
                    .setHighTargetCorner(Utils.dip2px(activity,15));
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {

                }

                @Override
                public void onDismiss() {
                    target.setBackgroundResource(R.drawable.bg_9a9999_round15);
                    //设置为不是第一次登陆
                    SharedPreferenceUtil.getInstance(activity).setIsShopGuideFirst(false);
                    if(target2 != null)
                        showGuideView2(activity,target2,shopGuideInterface);
                    else{
                        shopGuideInterface.onShopGuideDismiss();
                    }
                }
            });
            builder.addComponent(new ShopComponentFirst());
            Guide guide = builder.createGuide();
            guide.show(activity);
            Utils.mLogError("显示遮盖");

        }

    }

    /**
     * 信息提示
     * @param activity
     * @param target
     */
    public void showGuideView2(Activity activity, final View target, final ShopGuideInterface shopGuideInterface) {
        if (SharedPreferenceUtil.getInstance(activity).getIsShopGuideSecond()){
            target.setBackgroundResource(R.drawable.bg_white_round15);
            final GuideBuilder builder1 = new GuideBuilder();
            builder1.setTargetView(target)
                    .setAlpha(150)
                    .setHighTargetGraphStyle(Component.CIRCLE);
            builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {
                    target.setBackground(null);
                    shopGuideInterface.onShopGuideDismiss();
                }
            });

            builder1.addComponent(new ShopComponentSecond());
            Guide guide = builder1.createGuide();
            guide.show(activity);
            //设置为不是第二次登陆
            SharedPreferenceUtil.getInstance(activity).setIsShopGuideSecond(false);
        }
    }

}
