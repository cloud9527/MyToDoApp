package com.example.cloud.mytodoapp.taskdetail;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.cloud.mytodoapp.ActivityUtils;
import com.example.cloud.mytodoapp.BaseActivity;
import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.bean.Injection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;
    @BindView(R.id.fab_edit_task)
    FloatingActionButton mFabEditTask;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        initView();
        initDate();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInsatance(taskId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), taskDetailFragment, R.id.contentFrame);
        }

        new TaskDetailPresenter(taskId, Injection.provideTasksRepository(getApplicationContext()), taskDetailFragment);

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void initViewDate() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
