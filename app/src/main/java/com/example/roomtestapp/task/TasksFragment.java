package com.example.roomtestapp.task;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roomtestapp.R;
import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.data.source.TasksRepository;
import com.example.roomtestapp.data.source.local.TasksLocalDataSource;
import com.example.roomtestapp.data.source.local.ToDoDatabase;
import com.example.roomtestapp.databinding.FragmentTasksBinding;
import com.example.roomtestapp.task.adapter.TasksAdapter;
import com.example.roomtestapp.task.viewmodel.TasksViewModel;
import com.example.roomtestapp.util.AppExecutors;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;

    private FragmentTasksBinding fragmentTasksBinding;

    private TasksAdapter tasksAdapter;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentTasksBinding = FragmentTasksBinding.inflate(inflater, container, false);
        fragmentTasksBinding.setViewmodel(tasksViewModel);

        return fragmentTasksBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();

        setupListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        tasksViewModel.start();
    }

    public void setViewModel(TasksViewModel tasksViewModel) {
        this.tasksViewModel = tasksViewModel;
    }

    private void setupListAdapter() {
        ListView listView = fragmentTasksBinding.listTasks;

        ToDoDatabase toDoDatabase = ToDoDatabase.getInstance(getActivity().getApplicationContext());

        tasksAdapter = new TasksAdapter(
                new ArrayList<Task>(0),
                TasksRepository.getInstance(
                        TasksLocalDataSource.getInstance(new AppExecutors(),
                                toDoDatabase.taskDao())),
                tasksViewModel);
        listView.setAdapter(tasksAdapter);
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksViewModel.saveTask();
            }
        });
    }
}
