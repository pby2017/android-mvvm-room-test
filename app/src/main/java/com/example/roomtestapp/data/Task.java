package com.example.roomtestapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.UUID;

/**
 * Immutable model class for a Task.
 */
@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String id;

    @Nullable
    @ColumnInfo(name = "title")
    private final String title;

    @Nullable
    @ColumnInfo(name = "description")
    private final String description;

    @ColumnInfo(name = "completed")
    private final boolean completed;

    /**
     * Use this constructor to create a new active Task.
     *
     * @param title       title of the task
     * @param description description of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description) {
        this(title, description, UUID.randomUUID().toString(), false);
    }

    /**
     * Use this constructor to create an active Task if the Task already has an id (copy of another
     * Task).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @NonNull String id) {
        this(title, description, id, false);
    }

    /**
     * Use this constructor to create a new completed Task.
     *
     * @param title       title of the task
     * @param description description of the task
     * @param completed   true if the task is completed, false if it's active
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public Task(@Nullable String title, @Nullable String description,
                @NonNull String id, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Nullable
    public String getTitleForList() {
        if (!isNullOrEmpty(this.title)) {
            return this.title;
        } else {
            return this.description;
        }
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean isActive() {
        return !this.completed;
    }

    public boolean isEmpty() {
        return isNullOrEmpty(this.title) &&
                isNullOrEmpty(this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return equal(this.id, task.id) &&
                equal(this.title, task.title) &&
                equal(this.description, task.description);
    }

    @Override
    public int hashCode() {
        return hashCode(this.id, this.title, this.description);
    }

    @Override
    public String toString() {
        return "Task with title " + this.title;
    }

    private boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }

    private boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }
}
