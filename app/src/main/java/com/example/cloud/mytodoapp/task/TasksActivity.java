package com.example.cloud.mytodoapp.task;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cloud.mytodoapp.ActivityUtils;
import com.example.cloud.mytodoapp.BaseActivity;
import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.bean.Injection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentFrame)
    FrameLayout mContentFrame;
    @BindView(R.id.fab_add_task)
    FloatingActionButton mFabAddTask;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    private Bundle mSavedInstanceState1;
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState1 = savedInstanceState;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initDate();

    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        if (mNavView != null) {
            setNavViewListener(mNavView);

        }
    }

    @Override
    protected void initDate() {
        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        mTasksPresenter = new TasksPresenter(Injection.provideTasksRepository(getApplicationContext()), tasksFragment);
        // Load previously saved state, if available.
        if (mSavedInstanceState1 != null) {
            TasksFilterType currentFiltering =
                    (TasksFilterType) mSavedInstanceState1.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    protected void initViewDate() {

    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.fab_add_task)
    public void onClick() {
        mTasksPresenter.addNewTask();
    }


    private void setNavViewListener(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_navigation_menu_item:
                        Toast.makeText(TasksActivity.this, "list_navigation_menu_item", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.statistics_navigation_menu_item:
                        Toast.makeText(TasksActivity.this, "statistics_navigation_menu_item", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();

                return true;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
