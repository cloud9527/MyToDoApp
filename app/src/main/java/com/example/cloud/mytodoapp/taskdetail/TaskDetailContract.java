package com.example.cloud.mytodoapp.taskdetail;

import com.example.cloud.mytodoapp.BasePresenter;
import com.example.cloud.mytodoapp.BaseView;

/**
 * Created by Cloud on 2017/5/16.
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();

    }


    interface Presenter extends BasePresenter {
        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();

    }
}
