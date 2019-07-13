package com.example.roomtestapp.data.source;

import android.support.annotation.NonNull;

import com.example.roomtestapp.data.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource tasksLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private Map<String, Task> cachedTasks;

    private TasksRepository(TasksDataSource tasksLocalDataSource) {
        this.tasksLocalDataSource = tasksLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksLocalDataSource the device storage data source
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        if (callback == null) {
            throw new NullPointerException();
        }

        // Respond immediately with cache if available and not dirty
        if (cachedTasks != null) {
            callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            return;
        }

        // if(dirty cache) get tasks from remote

        tasksLocalDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                // get tasks from remote
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        if (task == null) {
            throw new NullPointerException();
        }
        tasksLocalDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.put(task.getId(), task);
    }

    private void refreshCache(List<Task> tasks) {
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
        cachedTasks.clear();
        for (Task task : tasks) {
            cachedTasks.put(task.getId(), task);
        }
    }
}
