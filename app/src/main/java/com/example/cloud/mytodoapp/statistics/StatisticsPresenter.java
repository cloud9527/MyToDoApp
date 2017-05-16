package com.example.cloud.mytodoapp.statistics;

import com.example.cloud.mytodoapp.bean.Task;
import com.example.cloud.mytodoapp.bean.source.TasksDataSource;
import com.example.cloud.mytodoapp.bean.source.TasksRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Cloud on 2017/5/16.
 */

public class StatisticsPresenter implements StatisticsContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final StatisticsContract.View mStatisticsView;


    public StatisticsPresenter(TasksRepository tasksRepository, StatisticsContract.View view) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null");
        mStatisticsView = checkNotNull(view, "StatisticsView cannot be null!");
        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadStatistics();
    }


    private void loadStatistics() {
        mStatisticsView.setProgressIndicator(true);

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int activeTasks = 0;
                int completedTasks = 0;

                // We calculate number of active and completed tasks
                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks += 1;
                    } else {
                        activeTasks += 1;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.setProgressIndicator(false);

                mStatisticsView.showStatistics(activeTasks, completedTasks);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.showLoadingStatisticsError();
            }
        });
    }
}
