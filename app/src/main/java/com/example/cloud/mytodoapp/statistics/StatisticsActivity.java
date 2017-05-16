package com.example.cloud.mytodoapp.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cloud.mytodoapp.BaseActivity;
import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.bean.Injection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsActivity extends BaseActivity implements StatisticsContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.statistics)
    TextView mStatistics;
    private StatisticsPresenter mStatisticsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);
        initView();
        initListener();
        initDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.statistics_title);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected void initDate() {
        mStatisticsPresenter = new StatisticsPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        mStatisticsPresenter.start();
    }

    @Override
    protected void initViewDate() {

    }

    @Override
    protected void initListener() {
        setupDrawerContent(mNavView);
    }

    public void setupDrawerContent(NavigationView upDrawerContent) {
        upDrawerContent.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_navigation_menu_item:
                        NavUtils.navigateUpFromSameTask(StatisticsActivity.this);
                        break;
                    case R.id.statistics_navigation_menu_item:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(StatisticsContract.Presenter presenter) {
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            mStatistics.setText(getString(R.string.loading));
        } else {
            mStatistics.setText("");
        }
    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {
            mStatistics.setText(getResources().getString(R.string.statistics_no_tasks));
        } else {
            String displayString = getResources().getString(R.string.statistics_active_tasks) + " "
                    + numberOfIncompleteTasks + "\n" + getResources().getString(
                    R.string.statistics_completed_tasks) + " " + numberOfCompletedTasks;
            mStatistics.setText(displayString);
        }
    }

    @Override
    public void showLoadingStatisticsError() {
        mStatistics.setText(getResources().getString(R.string.statistics_error));
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
