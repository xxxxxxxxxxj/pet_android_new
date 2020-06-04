package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择账单筛选时间界面
 */
public class BillTimeActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.tv_billtime_starttime)
    TextView tvBilltimeStarttime;
    @BindView(R.id.vw_billtime_starttime)
    View vwBilltimeStarttime;
    @BindView(R.id.tv_billtime_endtime)
    TextView tvBilltimeEndtime;
    @BindView(R.id.vw_billtime_endtime)
    View vwBilltimeEndtime;
    @BindView(R.id.wv_billtime_year)
    WheelView wvBilltimeYear;
    @BindView(R.id.wv_billtime_month)
    WheelView wvBilltimeMonth;
    private static final String[] months =
            {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] years;
    private String startYear = "";
    private String startMonth = "";
    private String endYear = "";
    private String endMonth = "";
    private int selectedStartYearIndex = -1;
    private int selectedStartMonthIndex = -1;
    private int selectedEndYearIndex = -1;
    private int selectedEndMonthIndex = -1;
    private String startYearTemp = "";
    private String startMonthTemp = "";
    private int selectedStartYearIndexTemp = -1;
    private int selectedStartMonthIndexTemp = -1;
    private int type = 1;
    private int localBeginYear;
    private int localEndYear;
    private int currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        MApplication.listAppoint.add(this);
        startYear = getIntent().getStringExtra("startYear");
        startMonth = getIntent().getStringExtra("startMonth");
        endYear = getIntent().getStringExtra("endYear");
        endMonth = getIntent().getStringExtra("endMonth");
        selectedStartYearIndex = getIntent().getIntExtra("selectedStartYearIndex", -1);
        selectedStartMonthIndex = getIntent().getIntExtra("selectedStartMonthIndex", -1);
        selectedEndYearIndex = getIntent().getIntExtra("selectedEndYearIndex", -1);
        selectedEndMonthIndex = getIntent().getIntExtra("selectedEndMonthIndex", -1);
        localBeginYear = getIntent().getIntExtra("localBeginYear", -1);
        localEndYear = getIntent().getIntExtra("localEndYear", -1);
        currentMonth = getIntent().getIntExtra("currentMonth", -1);
        years = new String[localEndYear - localBeginYear + 1];
        for (int i = 0; i <= (localEndYear - localBeginYear); i++) {
            years[i] = (localBeginYear + i) + "年";
        }
        if (!Utils.isStrNull(startMonth)) {
            selectedStartMonthIndex = currentMonth - 1;
            startMonth = months[selectedStartMonthIndex];
        }
        if (!Utils.isStrNull(endMonth)) {
            selectedEndMonthIndex = currentMonth - 1;
            endMonth = months[selectedEndMonthIndex];
        }
        if (selectedStartYearIndex < 0) {
            selectedStartYearIndex = years.length - 1;
            startYear = years[selectedStartYearIndex];
        }
        if (selectedStartMonthIndex < 0) {
            selectedStartMonthIndex = months.length - 1;
            startMonth = months[selectedStartMonthIndex];
        }
        if (selectedEndYearIndex < 0) {
            selectedEndYearIndex = years.length - 1;
            endYear = years[selectedEndYearIndex];
        }
        if (selectedEndMonthIndex < 0) {
            selectedEndMonthIndex = months.length - 1;
            endMonth = months[selectedEndMonthIndex];
        }
    }

    private void findView() {
        setContentView(R.layout.activity_bill_time);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("选择时间");
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("完成");
        tvTitlebarOther.setTextColor(getResources().getColor(R.color.white));
        wvBilltimeYear.setLineSpacingMultiplier(1.5f);
        wvBilltimeYear.setTextSize(23 * getResources().getDisplayMetrics().density / 3);
        wvBilltimeYear.setTextColorCenter(getResources().getColor(R.color.a333333));
        wvBilltimeYear.setTextColorOut(getResources().getColor(R.color.a999999));
        wvBilltimeYear.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(years)));
        wvBilltimeYear.setCyclic(false);//循环滚动
        wvBilltimeYear.setDividerColor(getResources().getColor(R.color.a979797));
        wvBilltimeYear.smoothScroll(WheelView.ACTION.FLING);
        wvBilltimeMonth.setLineSpacingMultiplier(1.5f);
        wvBilltimeMonth.setTextSize(23 * getResources().getDisplayMetrics().density / 3);
        wvBilltimeMonth.setTextColorCenter(getResources().getColor(R.color.a333333));
        wvBilltimeMonth.setTextColorOut(getResources().getColor(R.color.a999999));
        wvBilltimeMonth.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(months)));
        wvBilltimeMonth.setCyclic(false);//循环滚动
        wvBilltimeMonth.setDividerColor(getResources().getColor(R.color.a979797));
        wvBilltimeMonth.smoothScroll(WheelView.ACTION.FLING);
        type = 1;
        setTime();
        endYear = endYear.replace("年", "");
        endMonth = endMonth.replace("月", "");
        tvBilltimeEndtime.setText(endYear + "-" + endMonth);
    }

    private void setTime() {
        wvBilltimeYear.setVisibility(View.VISIBLE);
        wvBilltimeMonth.setVisibility(View.VISIBLE);
        if (type == 1) {
            if (!Utils.isStrNull(startMonth)) {
                selectedStartMonthIndex = currentMonth - 1;
                startMonth = months[selectedStartMonthIndex];
            }
            if (selectedStartYearIndex < 0) {
                selectedStartYearIndex = years.length - 1;
                startYear = years[selectedStartYearIndex];
            }
            if (selectedStartMonthIndex < 0) {
                selectedStartMonthIndex = months.length - 1;
                startMonth = months[selectedStartMonthIndex];
            }
            startYear = startYear.replace("年", "");
            startMonth = startMonth.replace("月", "");
            tvBilltimeStarttime.setText(startYear + "-" + startMonth);
            wvBilltimeYear.setCurrentItem(selectedStartYearIndex);
            wvBilltimeMonth.setCurrentItem(selectedStartMonthIndex);
            tvBilltimeStarttime.setTextColor(getResources().getColor(R.color.aBB996C));
            tvBilltimeEndtime.setTextColor(getResources().getColor(R.color.aBBBBBB));
            vwBilltimeStarttime.setBackgroundColor(getResources().getColor(R.color.aBB996C));
            vwBilltimeEndtime.setBackgroundColor(getResources().getColor(R.color.aBBBBBB));
        } else if (type == 2) {
            if (!Utils.isStrNull(endMonth)) {
                selectedEndMonthIndex = currentMonth - 1;
                endMonth = months[selectedEndMonthIndex];
            }
            if (selectedEndYearIndex < 0) {
                selectedEndYearIndex = years.length - 1;
                endYear = years[selectedEndYearIndex];
            }
            if (selectedEndMonthIndex < 0) {
                selectedEndMonthIndex = months.length - 1;
                endMonth = months[selectedEndMonthIndex];
            }
            endYear = endYear.replace("年", "");
            endMonth = endMonth.replace("月", "");
            tvBilltimeEndtime.setText(endYear + "-" + endMonth);
            wvBilltimeYear.setCurrentItem(selectedEndYearIndex);
            wvBilltimeMonth.setCurrentItem(selectedEndMonthIndex);
            tvBilltimeStarttime.setTextColor(getResources().getColor(R.color.aBBBBBB));
            tvBilltimeEndtime.setTextColor(getResources().getColor(R.color.aBB996C));
            vwBilltimeStarttime.setBackgroundColor(getResources().getColor(R.color.aBBBBBB));
            vwBilltimeEndtime.setBackgroundColor(getResources().getColor(R.color.aBB996C));
        }
    }

    private void setLinster() {
        wvBilltimeYear.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (type == 1) {
                    startYear = years[index];
                    selectedStartYearIndex = index;
                    if (Utils.isStrNull(startYear)) {
                        startYear = startYear.replace("年", "");
                    }
                    if (Utils.isStrNull(startMonth)) {
                        startMonth = startMonth.replace("月", "");
                    }
                    tvBilltimeStarttime.setText(startYear + "-" + startMonth);
                } else if (type == 2) {
                    endYear = years[index];
                    selectedEndYearIndex = index;
                    if (Utils.isStrNull(endYear)) {
                        endYear = endYear.replace("年", "");
                    }
                    if (Utils.isStrNull(endMonth)) {
                        endMonth = endMonth.replace("月", "");
                    }
                    tvBilltimeEndtime.setText(endYear + "-" + endMonth);
                }
            }
        });
        wvBilltimeMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (type == 1) {
                    startMonth = months[index];
                    selectedStartMonthIndex = index;
                    if (Utils.isStrNull(startYear)) {
                        startYear = startYear.replace("年", "");
                    }
                    if (Utils.isStrNull(startMonth)) {
                        startMonth = startMonth.replace("月", "");
                    }
                    tvBilltimeStarttime.setText(startYear + "-" + startMonth);
                } else if (type == 2) {
                    endMonth = months[index];
                    selectedEndMonthIndex = index;
                    if (Utils.isStrNull(endYear)) {
                        endYear = endYear.replace("年", "");
                    }
                    if (Utils.isStrNull(endMonth)) {
                        endMonth = endMonth.replace("月", "");
                    }
                    tvBilltimeEndtime.setText(endYear + "-" + endMonth);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.ll_billtime_starttime, R.id.ll_billtime_endtime, R.id.iv_billtime_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                if (!(Utils.isStrNull(startYear) && Utils.isStrNull(startMonth))) {
                    ToastUtil.showToastShortBottom(this, "请选择开始时间");
                    return;
                }
                if (!(Utils.isStrNull(endYear) && Utils.isStrNull(endMonth))) {
                    ToastUtil.showToastShortBottom(this, "请选择结束时间");
                    return;
                }
                if (Integer.parseInt(endYear) < Integer.parseInt(startYear)) {
                    //调换
                    startYearTemp = startYear;
                    startMonthTemp = startMonth;
                    selectedStartYearIndexTemp = selectedStartYearIndex;
                    selectedStartMonthIndexTemp = selectedStartMonthIndex;

                    startYear = endYear;
                    startMonth = endMonth;
                    selectedStartYearIndex = selectedEndYearIndex;
                    selectedStartMonthIndex = selectedEndMonthIndex;

                    endYear = startYearTemp;
                    endMonth = startMonthTemp;
                    selectedEndYearIndex = selectedStartYearIndexTemp;
                    selectedEndMonthIndex = selectedStartMonthIndexTemp;
                } else if (Integer.parseInt(endYear) == Integer.parseInt(startYear)) {
                    if (Integer.parseInt(endMonth) < Integer.parseInt(startMonth)) {
                        //调换
                        startYearTemp = startYear;
                        startMonthTemp = startMonth;
                        selectedStartYearIndexTemp = selectedStartYearIndex;
                        selectedStartMonthIndexTemp = selectedStartMonthIndex;

                        startYear = endYear;
                        startMonth = endMonth;
                        selectedStartYearIndex = selectedEndYearIndex;
                        selectedStartMonthIndex = selectedEndMonthIndex;

                        endYear = startYearTemp;
                        endMonth = startMonthTemp;
                        selectedEndYearIndex = selectedStartYearIndexTemp;
                        selectedEndMonthIndex = selectedStartMonthIndexTemp;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("startYear", startYear);
                intent.putExtra("startMonth", startMonth);
                intent.putExtra("endYear", endYear);
                intent.putExtra("endMonth", endMonth);
                intent.putExtra("selectedStartYearIndex", selectedStartYearIndex);
                intent.putExtra("selectedStartMonthIndex", selectedStartMonthIndex);
                intent.putExtra("selectedEndYearIndex", selectedEndYearIndex);
                intent.putExtra("selectedEndMonthIndex", selectedEndMonthIndex);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ll_billtime_starttime:
                type = 1;
                setTime();
                break;
            case R.id.ll_billtime_endtime:
                type = 2;
                setTime();
                break;
            case R.id.iv_billtime_clear:
                tvBilltimeStarttime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                tvBilltimeEndtime.setTextColor(getResources().getColor(R.color.aBBBBBB));
                vwBilltimeStarttime.setBackgroundColor(getResources().getColor(R.color.aBBBBBB));
                vwBilltimeEndtime.setBackgroundColor(getResources().getColor(R.color.aBBBBBB));
                tvBilltimeStarttime.setText("开始时间");
                tvBilltimeEndtime.setText("结束时间");
                wvBilltimeYear.setVisibility(View.INVISIBLE);
                wvBilltimeMonth.setVisibility(View.INVISIBLE);
                startYear = "";
                startMonth = "";
                endYear = "";
                endMonth = "";
                selectedEndYearIndex = -1;
                selectedStartYearIndex = -1;
                selectedEndMonthIndex = -1;
                selectedStartMonthIndex = -1;
                break;
        }
    }
}
