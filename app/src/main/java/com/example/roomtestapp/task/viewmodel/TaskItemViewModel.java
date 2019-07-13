package com.example.roomtestapp.task.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.example.roomtestapp.R;
import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.data.source.TasksRepository;

public class TaskItemViewModel {
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    private final ObservableField<Task> taskObservable = new ObservableField<>();

    private final TasksRepository tasksRepository;

    private final Context context;

    public TaskItemViewModel(Context context, TasksRepository tasksRepository) {
        this.context = context;
        this.tasksRepository = tasksRepository;

        taskObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Task task = taskObservable.get();
                if (task != null) {
                    title.set(task.getTitle());
                    description.set(task.getDescription());
                } else {
                    title.set(TaskItemViewModel.this.context.getString(R.string.no_data));
                    description.set(TaskItemViewModel.this.context.getString(R.string.no_data_description));
                }
            }
        });
    }

    public void setTask(Task task) {
        taskObservable.set(task);
    }
}
