package com.example.cloud.mytodoapp.addTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cloud.mytodoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Cloud on 2017/5/12.
 */

public class AddTaskFragment extends Fragment implements AddEditTaskContract.View {
    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
    @BindView(R.id.add_task_title)
    EditText mAddTaskTitle;
    @BindView(R.id.add_task_description)
    EditText mAddTaskDescription;
    Unbinder unbinder;

    private AddTaskPresenter mAddTaskPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddTaskPresenter.start();
    }

    public AddTaskFragment() {
    }

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton actionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        actionButton.setImageResource(R.drawable.ic_done);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddTaskPresenter.saveTask(mAddTaskTitle.getText().toString(), mAddTaskDescription.getText().toString());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addtask_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mAddTaskPresenter = (AddTaskPresenter) checkNotNull(presenter);
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mAddTaskTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mAddTaskTitle.setText(title);
    }

    @Override
    public void setDescription(String description) {
        mAddTaskDescription.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
