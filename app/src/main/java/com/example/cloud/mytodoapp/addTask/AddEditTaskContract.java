package com.example.cloud.mytodoapp.addTask;

import com.example.cloud.mytodoapp.BasePresenter;
import com.example.cloud.mytodoapp.BaseView;

/**
 * Created by Cloud on 2017/5/12.
 */

public interface AddEditTaskContract {
    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}
