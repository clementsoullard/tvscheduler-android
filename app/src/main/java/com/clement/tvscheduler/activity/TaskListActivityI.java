package com.clement.tvscheduler.activity;

import com.clement.tvscheduler.object.Task;

import java.util.List;

/**
 * Created by cleme on 29/04/2017.
 */

public interface TaskListActivityI extends ConnectedActivityI {
    Object getSystemService(String layoutInflaterService);

    void askConfirmationBeforeRemoving(String id, String name);

    void refreshTaskList();

    void setTodos(List<Task> tasks);

}

