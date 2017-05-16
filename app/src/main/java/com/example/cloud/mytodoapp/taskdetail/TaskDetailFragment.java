package com.example.cloud.mytodoapp.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.cloud.mytodoapp.R;
import com.example.cloud.mytodoapp.addTask.AddTaskActivity;
import com.example.cloud.mytodoapp.addTask.AddTaskFragment;
import com.google.common.base.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Cloud on 2017/5/16.
 */

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {
    @NonNull
    private static final String ARGUMENT_TASK_ID = "TASK_ID";

    @NonNull
    private static final int REQUEST_EDIT_TASK = 1;

    @BindView(R.id.task_detail_complete)
    CheckBox mTaskDetailComplete;
    @BindView(R.id.task_detail_title)
    TextView mTaskDetailTitle;
    @BindView(R.id.task_detail_description)
    TextView mTaskDetailDescription;
    Unbinder unbinder;


    private TaskDetailContract.Presenter mPresenter;

    public static TaskDetailFragment newInsatance(String taskId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        taskDetailFragment.setArguments(bundle);
        return taskDetailFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taskdetail_frag, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editTask();
            }
        });
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mPresenter.deleteTask();
                break;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_detail_menu, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }


    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mTaskDetailTitle.setText("");
            mTaskDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingTask() {
        mTaskDetailTitle.setText("");
        mTaskDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void hideTitle() {
        mTaskDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        mTaskDetailTitle.setVisibility(View.VISIBLE);
        mTaskDetailTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        mTaskDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mTaskDetailDescription.setVisibility(View.VISIBLE);
        mTaskDetailDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        Preconditions.checkNotNull(mTaskDetailComplete);
        mTaskDetailComplete.setChecked(complete);
        mTaskDetailComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.completeTask();
                } else {
                    mPresenter.activateTask();
                }
            }
        });
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
