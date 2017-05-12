package com.example.cloud.mytodoapp.addTask;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.cloud.mytodoapp.ActivityUtils;
import com.example.cloud.mytodoapp.BaseActivity;
import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.bean.Injection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskActivity extends BaseActivity {
    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab_edit_task_done)
    FloatingActionButton mFabEditTaskDone;
    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private ActionBar mActionBar;

    private AddTaskPresenter mAddEditTaskPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        initView(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    protected void initView(Bundle savedInstanceState) {
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddTaskFragment taskFragment = (AddTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String taskId = getIntent().getStringExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID);
        setToolbarTitle(taskId);
        if (taskFragment == null) {
            taskFragment = AddTaskFragment.newInstance();
            if (getIntent().hasExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                taskFragment.setArguments(bundle);
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), taskFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditTaskPresenter = new AddTaskPresenter(
                taskId,
                Injection.provideTasksRepository(getApplicationContext()),
                taskFragment,
                shouldLoadDataFromRepo);
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

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            mActionBar.setTitle(R.string.add_task);
        } else {
            mActionBar.setTitle(R.string.edit_task);
        }
    }
}
