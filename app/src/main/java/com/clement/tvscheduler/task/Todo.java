package com.clement.tvscheduler.task;

/**
 * Created by cleme on 29/10/2016.
 */
public class Todo {
    String name;

    Boolean done;

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {
        return done;
    }

    public String getName() {
        return name;
    }
}
