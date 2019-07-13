package com.example.roomtestapp.task;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.roomtestapp.R;
import com.example.roomtestapp.data.source.TasksRepository;
import com.example.roomtestapp.data.source.local.TasksLocalDataSource;
import com.example.roomtestapp.data.source.local.ToDoDatabase;
import com.example.roomtestapp.task.viewmodel.TasksViewModel;
import com.example.roomtestapp.util.AppExecutors;

public class TasksActivity extends AppCompatActivity {

    private TasksViewModel tasksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        TasksFragment tasksFragment = TasksFragment.newInstance();

        ToDoDatabase toDoDatabase = ToDoDatabase.getInstance(getApplicationContext());

        tasksViewModel = new TasksViewModel(TasksRepository.getInstance(
                TasksLocalDataSource.getInstance(
                        new AppExecutors(), toDoDatabase.taskDao())));
        tasksFragment.setViewModel(tasksViewModel);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_content, tasksFragment);
        transaction.commit();
    }
}