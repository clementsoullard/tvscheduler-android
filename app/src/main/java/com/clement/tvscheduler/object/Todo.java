package com.clement.tvscheduler.object;

import java.util.Date;

/**
 * Created by cleme on 29/10/2016.
 */
public class Todo {
    String name;

    String id;

    Boolean done;


   String owner;

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {

        if (done == null) {
            return false;
        }
        return done;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

     public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
