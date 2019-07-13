package com.example.roomtestapp.task;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.task.adapter.TasksAdapter;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Task} list.
 */
public class TasksListBindings {

    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Task> items) {
        TasksAdapter adapter = (TasksAdapter) listView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }
}
