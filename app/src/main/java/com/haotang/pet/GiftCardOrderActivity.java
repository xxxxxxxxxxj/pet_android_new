package com.haotang.pet;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.haotang.base.BaseFragmentActivity;
import com.haotang.pet.fragment.GiftCardOrderFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GiftCardOrderActivity extends BaseFragmentActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.stl_giftcard_order)
    SlidingTabLayout stlGiftcardOrder;
    @BindView(R.id.vp_giftcard_order)
    ViewPager vpGiftcardOrder;
    private int[] colors =new int[2];
    public static GiftCardOrderActivity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        findView();
        setView();
    }

    private void setView() {
        tvTitlebarTitle.setText("卡订单");
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("全部", GiftCardOrderFragment.class)
                .add("退款中", GiftCardOrderFragment.class)
                .add("已退款", GiftCardOrderFragment.class)
                .add("已完成", GiftCardOrderFragment.class)
                .create());
        stlGiftcardOrder.setmTextSelectsize(stlGiftcardOrder.sp2px(16));
        stlGiftcardOrder.setGradient(true);
        colors[0] = getResources().getColor(R.color.aeb6340);
        colors[1] = getResources().getColor(R.color.ae5287b);
        stlGiftcardOrder.setColors(colors);
        stlGiftcardOrder.setIndicatorTextMiddle(true);
        vpGiftcardOrder.setAdapter(adapter);
        stlGiftcardOrder.setViewPager(vpGiftcardOrder);
        stlGiftcardOrder.setCurrentTab(0);
        vpGiftcardOrder.setCurrentItem(0);
    }

    private void findView() {
        setContentView(R.layout.activity_gift_card_order);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
