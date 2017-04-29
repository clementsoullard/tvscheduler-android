package com.clement.tvscheduler.task.todo;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.clement.tvscheduler.activity.MainActivity;
import com.clement.tvscheduler.activity.TaskListActivityI;
import com.clement.tvscheduler.object.Todo;
import com.clement.tvscheduler.task.BaseTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class ListTodoTask extends BaseTask {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private List<Todo> todos;
    private String messageRetour;
    private TaskListActivityI taskListActivity;
    private String taskOwner;

    public ListTodoTask(TaskListActivityI mainActivity, String taskOwner) {
        super(mainActivity);
        this.taskListActivity = mainActivity;
        this.taskOwner = taskOwner;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(MainActivity.TAG, "Execution " + this.getClass());
            String uri = "/tvscheduler/today-tasks";
            if (taskOwner.equals("home")) {
                uri += "-home";
            }
            InputStream is = getHttpUrlConnection(uri).getInputStream();
            readJsonStream(is);
            messageRetour = "Succès";
            return 0L;
        } catch (Exception e) {
            Log.e(MainActivity.TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        Log.i(MainActivity.TAG, "Taches retournées avec succès");
        if (todos == null) {
            taskListActivity.showMessage("Erreur de service");
            return;
        }
        for (Todo todo : todos) {
            Log.i(MainActivity.TAG, "Tache: " + todo.getName());
        }
        taskListActivity.setTodos(todos);

    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public List<Todo> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readTodos(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * This reads the json.
     *
     * @param reader
     * @return
     * @throws IOException
     */
    public List<Todo> readTodos(JsonReader reader) throws IOException {
        Log.d(MainActivity.TAG, "Decryptage des Todo du jour");
        todos = new ArrayList<Todo>();

        reader.beginArray();
        while (reader.hasNext()) {
            Todo todo = readTodo(reader);
            todos.add(todo);
        }
        return todos;
    }

    private Todo readTodo(JsonReader reader) throws IOException {
        Todo todo = new Todo();
        reader.beginObject();
        String name = null;
        String id = null;
        String date = null;
        String owner = null;
        Boolean done = null;


        while (reader.hasNext()) {
            String nameJson = reader.nextName();
            JsonToken look = reader.peek();

            if (look == JsonToken.NULL) {
                reader.skipValue();
            } else if (nameJson.equals("taskName")) {
                name = reader.nextString();
            } else if (nameJson.equals("done")) {
                done = reader.nextBoolean();
            } else if (nameJson.equals("idr")) {
                id = reader.nextString();
            } else if (nameJson.equals("date")) {
                date = reader.nextString();
            } else if (nameJson.equals("owner")) {
                owner = reader.nextString();
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        todo.setDone(done);
        todo.setName(name);
        todo.setId(id);

        return todo;
    }

}