package com.example.roomtestapp.data.source;

import android.support.annotation.NonNull;

import com.example.roomtestapp.data.Task;

import java.util.List;

public interface TasksDataSource {

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(@NonNull Task task);

    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }
}
