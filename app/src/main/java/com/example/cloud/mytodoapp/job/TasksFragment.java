package com.example.cloud.mytodoapp.job;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.bean.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Cloud on 2017/5/10.
 */

public class TasksFragment extends Fragment implements TasksContract.View {

    @BindView(R.id.filteringLabel)
    TextView mFilteringLabel;
    @BindView(R.id.tasks_list)
    ListView mTasksList;
    @BindView(R.id.tasksLL)
    LinearLayout mTasksLL;
    @BindView(R.id.noTasksIcon)
    ImageView mNoTasksIcon;
    @BindView(R.id.noTasksMain)
    TextView mNoTasksMain;
    @BindView(R.id.noTasksAdd)
    TextView mNoTasksAdd;
    @BindView(R.id.noTasks)
    LinearLayout mNoTasks;
    @BindView(R.id.tasksContainer)
    RelativeLayout mTasksContainer;
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;


    private TasksContract.Presenter mPresenter;
    private JobAdapter mJobAdapter;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJobAdapter = new JobAdapter(new ArrayList<Task>(0), mJobItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_job, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mTasksList.setAdapter(mJobAdapter);
        initListener();
        return rootView;
    }

    private void initListener() {
        mNoTasksAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTask();
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        floatingActionButton.setImageResource(R.drawable.ic_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });
        mRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(false);
            }
        });
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mJobAdapter.replcaeData(tasks);

        mTasksLL.setVisibility(View.VISIBLE);
        mNoTasks.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {
        Toast.makeText(getActivity(), "showAddTask", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Toast.makeText(getActivity(), "taskId", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showActiveFilterLabel() {
        mFilteringLabel.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        mFilteringLabel.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        mFilteringLabel.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.filter_tasks, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });
        popupMenu.show();
    }


    public interface JobItemListener {

        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }


    public static class JobAdapter extends BaseAdapter {
        private List<Task> mTasks;
        private JobItemListener mJobItemListener;

        public JobAdapter(List<Task> tasks, JobItemListener jobItemListener) {
            mTasks = tasks;
            mJobItemListener = jobItemListener;
        }


        public void replcaeData(List<Task> tasks) {
            setList(tasks);
            notifyDataSetChanged();
        }

        private void setList(List<Task> tasks) {
            mTasks = checkNotNull(tasks);
        }


        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Object getItem(int position) {
            return mTasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Task task = mTasks.get(position);
            viewHolder.mTitle.setText(task.getTitleForList());
            viewHolder.mComplete.setChecked(task.isCompleted());
            if (task.isCompleted()) {
                convertView.setBackgroundDrawable(parent.getContext()
                        .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                convertView.setBackgroundDrawable(parent.getContext()
                        .getResources().getDrawable(R.drawable.touch_feedback));
            }
            viewHolder.mComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!task.isCompleted()) {
                        mJobItemListener.onCompleteTaskClick(task);
                    } else {
                        mJobItemListener.onActivateTaskClick(task);
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mJobItemListener.onTaskClick(task);
                }
            });

            return convertView;
        }

        public static class ViewHolder {
            @BindView(R.id.complete)
            CheckBox mComplete;
            @BindView(R.id.title)
            TextView mTitle;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    JobItemListener mJobItemListener = new JobItemListener() {

        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };


    private void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mTasksLL.setVisibility(View.GONE);
        mNoTasksIcon.setVisibility(View.VISIBLE);

        mNoTasksMain.setText(mainText);
        mNoTasksIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoTasksAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

}
