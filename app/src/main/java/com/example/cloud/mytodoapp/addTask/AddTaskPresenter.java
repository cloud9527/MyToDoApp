package com.example.cloud.mytodoapp.addTask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.cloud.mytodoapp.bean.Task;
import com.example.cloud.mytodoapp.bean.source.TasksDataSource;
import com.example.cloud.mytodoapp.bean.source.TasksRepository;

/**
 * Created by Cloud on 2017/5/12.
 */

public class AddTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {
    private TasksDataSource mTasksRepository;
    @NonNull
    private AddEditTaskContract.View mAddTaskView;
    @Nullable
    private String mTaskId;
    private boolean mIsDataMissing;

    public AddTaskPresenter(String taskId, TasksRepository tasksRepository
            , AddEditTaskContract.View view, boolean shouldLoadDataFromRepo) {
        mTaskId = taskId;
        mTasksRepository = tasksRepository;
        mAddTaskView = view;
        mIsDataMissing = shouldLoadDataFromRepo;
    }

    @Override
    public void start() {
        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public boolean isDataMissing() {
        return false;
    }


    @Override
    public void onTaskLoaded(Task task) {
        if (mAddTaskView.isActive()) {
            mAddTaskView.setTitle(task.getTitle());
            mAddTaskView.setDescription(task.getDescription());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError();
        }
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }


    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksRepository.saveTask(newTask);
            mAddTaskView.showTasksList();
        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mTasksRepository.saveTask(new Task(title, description, mTaskId));
        mAddTaskView.showTasksList(); // After an edit, go back to the list.
    }
}
