package com.example.roomtestapp.task.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.data.source.TasksDataSource;
import com.example.roomtestapp.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends BaseObservable {

    public final ObservableList<Task> items = new ObservableArrayList<>();

    private final TasksRepository tasksRepository;

    public TasksViewModel(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public void start() {
        loadTasks();
    }

    public void loadTasks() {

        tasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                // We filter the tasks based on the requestType
                tasksToShow.addAll(tasks);

                items.clear();
                items.addAll(tasksToShow);

                // BR.empty = 10
                notifyPropertyChanged(10); // It's a @Bindable so update manually
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    // Called when clicking on fab.
    public void saveTask() {
        createTask("title", "description");
    }

    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        tasksRepository.saveTask(newTask);
        loadTasks();
    }
}
