package com.example.roomtestapp.data.source.local;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.data.source.TasksDataSource;
import com.example.roomtestapp.util.AppExecutors;

import java.util.List;

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDao tasksDao;

    private AppExecutors appExecutors;

    private TasksLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull TasksDao tasksDao) {
        this.appExecutors = appExecutors;
        this.tasksDao = tasksDao;
    }

    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = tasksDao.getTasks();
                Runnable getRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTasksLoaded(tasks);
                        }

                        Log.d(TasksLocalDataSource.class.getSimpleName(),
                                String.format("[%s] Looper.myLooper() = %s, Thread.currentThread() = %s",
                                        "getRunnable"
                                        , Looper.myLooper() != null ? Looper.myLooper().toString() : "null"
                                        , Thread.currentThread().getName()));
                    }
                };
                Log.d(TasksLocalDataSource.class.getSimpleName(),
                        String.format("[%s] Looper.myLooper() = %s, Thread.currentThread() = %s",
                                "runnable"
                                , Looper.myLooper() != null ? Looper.myLooper().toString() : "null"
                                , Thread.currentThread().getName()));

                // AccessDatabase 1
//                getRunnable.run();

                // AccessDatabase 2
                appExecutors.mainThread().execute(getRunnable);
            }
        };

        // AccessDatabase 1 : Cannot access database on the main thread
//        runnable.run();

        // AccessDatabase 2
        appExecutors.diskIO().execute(runnable);

        // AccessDatabase 3 : RxJava

        Log.d(TasksLocalDataSource.class.getSimpleName(),
                String.format("[%s] Looper.myLooper() = %s, Thread.currentThread() = %s",
                        "TasksLocalDataSource.getTasks()"
                        , Looper.myLooper() != null ? Looper.myLooper().toString() : "null"
                        , Thread.currentThread().getName()));
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        if (task == null) {
            throw new NullPointerException();
        }
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                tasksDao.insertTask(task);

                Log.d(TasksLocalDataSource.class.getSimpleName(),
                        String.format("[%s] Looper.myLooper() = %s, Thread.currentThread() = %s",
                                "saveRunnable"
                                , Looper.myLooper() != null ? Looper.myLooper().toString() : "null"
                                , Thread.currentThread().getName()));
            }
        };

        // AccessDatabase 1 : Cannot access database on the main thread
//        saveRunnable.run();
//        tasksDao.insertTask(task);

        // AccessDatabase 2
        appExecutors.diskIO().execute(saveRunnable);

        // AccessDatabase 3 : AsyncTask (old style)
//        new SaveTask(task).execute();

        // AccessDatabase 4 : RxJava

        Log.d(TasksLocalDataSource.class.getSimpleName(),
                String.format("[%s] Looper.myLooper() = %s, Thread.currentThread() = %s",
                        "TasksLocalDataSource.saveTask()"
                        , Looper.myLooper() != null ? Looper.myLooper().toString() : "null"
                        , Thread.currentThread().getName()));
    }

    class SaveTask extends AsyncTask<Void, Void, Void> {

        private final Task task;

        public SaveTask(Task task) {
            this.task = task;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tasksDao.insertTask(task);
            return null;
        }
    }
}
