package com.example.roomtestapp.task.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.roomtestapp.data.Task;
import com.example.roomtestapp.data.source.TasksRepository;
import com.example.roomtestapp.databinding.ItemTaskBinding;
import com.example.roomtestapp.task.viewmodel.TaskItemViewModel;
import com.example.roomtestapp.task.viewmodel.TasksViewModel;

import java.util.List;

public class TasksAdapter extends BaseAdapter {

    private final TasksViewModel tasksViewModel;

    private List<Task> tasks;

    private TasksRepository tasksRepository;

    public TasksAdapter(List<Task> tasks, TasksRepository tasksRepository, TasksViewModel tasksViewModel) {
        this.tasks = tasks;
        this.tasksRepository = tasksRepository;
        this.tasksViewModel = tasksViewModel;
        setList(tasks);
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
    }

    @Override
    public int getCount() {
        return this.tasks != null ? this.tasks.size() : 0;
    }

    @Override
    public Task getItem(int position) {
        return this.tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Task task = getItem(position);
        ItemTaskBinding itemTaskBinding;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            itemTaskBinding = ItemTaskBinding.inflate(inflater, viewGroup, false);
        } else {
            itemTaskBinding = DataBindingUtil.getBinding(view);
        }

        final TaskItemViewModel taskItemViewModel = new TaskItemViewModel(
                viewGroup.getContext().getApplicationContext(),
                tasksRepository);

        itemTaskBinding.setViewmodel(taskItemViewModel);

        taskItemViewModel.setTask(task);

        return itemTaskBinding.getRoot();
    }

    private void setList(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }
}