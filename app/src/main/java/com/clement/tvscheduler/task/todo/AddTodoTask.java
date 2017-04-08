package com.clement.tvscheduler.task.todo;

import android.util.Log;

import com.clement.tvscheduler.activity.ConnectedActivity;
import com.clement.tvscheduler.activity.CreateTaskActivity;
import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.activity.MainActivity;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.object.Todo;
import com.clement.tvscheduler.task.BaseTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This task is to add an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class AddTodoTask extends BaseTask {


    private String messageRetour;

    private Todo achat;

    private CreateTaskActivity createTaskActivity;

    /**
     *
     *
     */

    public AddTodoTask(CreateTaskActivity createTaskActivity, Todo todo) {
        super(createTaskActivity);
        this.createTaskActivity = createTaskActivity;
        this.achat = todo;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/ws-create-todo");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
          /*
             * JSON
             */
            JSONObject root = new JSONObject();
            root.put("taskName", achat.getName());
            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            int responseCode = urlConnection.getResponseCode();
            messageRetour = "Succès";
            return 0L;
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
        createTaskActivity.todoEnregistre();
    }


}