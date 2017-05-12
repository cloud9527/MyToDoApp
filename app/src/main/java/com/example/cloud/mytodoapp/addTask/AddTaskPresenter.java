package com.example.cloud.mytodoapp.addTask;

import com.example.cloud.mytodoapp.bean.Task;
import com.example.cloud.mytodoapp.bean.source.TasksDataSource;
import com.example.cloud.mytodoapp.bean.source.TasksRepository;

/**
 * Created by Cloud on 2017/5/12.
 */

public class AddTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {
    public AddTaskPresenter(String taskId, TasksRepository tasksRepository
            , AddTaskFragment taskFragment, boolean shouldLoadDataFromRepo) {

    }

    @Override
    public void start() {

    }

    @Override
    public void saveTask(String title, String description) {

    }

    @Override
    public void populateTask() {

    }

    @Override
    public boolean isDataMissing() {
        return false;
    }


    @Override
    public void onTaskLoaded(Task task) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
