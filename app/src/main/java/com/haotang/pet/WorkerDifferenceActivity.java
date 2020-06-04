package com.haotang.pet;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.WorkerDifferenceAdapter;
import com.haotang.pet.entity.WorkerDifference;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 美容师区别页面
 */
public class WorkerDifferenceActivity extends SuperActivity {
    private List<WorkerDifference> differenceList;
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_worker_difference)
    RecyclerView rvWorkerDifference;
    private WorkerDifferenceAdapter workerDifferenceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        act = this;
        MApplication.listAppoint.add(this);
        differenceList = (List<WorkerDifference>) getIntent().getSerializableExtra("differenceList");
    }

    private void findView() {
        setContentView(R.layout.activity_worker_difference);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("美容师区别");
        rvWorkerDifference.setHasFixedSize(true);
        rvWorkerDifference.setLayoutManager(new LinearLayoutManager(this));
        workerDifferenceAdapter = new WorkerDifferenceAdapter(R.layout.item_workerdifference, differenceList);
        rvWorkerDifference.setAdapter(workerDifferenceAdapter);
    }

    private void setLinster() {

    }

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
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
}
