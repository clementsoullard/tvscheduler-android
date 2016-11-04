package com.clement.tvscheduler.task;

import android.util.Log;

import com.clement.tvscheduler.MainActivity;
import com.clement.tvscheduler.object.Todo;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class UpdateTodoTask extends BaseTask {


    private String messageRetour;

    private String baserUrl;

    private Todo todo;


    public UpdateTodoTask(MainActivity mainActivity, Todo todo) {
        super(mainActivity);
        this.todo=todo;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/repository/task/"+todo.getId());
            urlConnection.setRequestMethod("PATCH");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
             */

         JSONObject root = new JSONObject();
            root.put("id", todo.getId());
            root.put("done", todo.getDone());
            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            messageRetour = "Succès";

            int responseCode = urlConnection.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                Log.e(MainActivity.TAG, "14 - HTTP_OK");
            } else {
                Log.e(MainActivity.TAG, responseCode + "  - False - HTTP_OK");
                messageRetour = "Service non disponible";
            }
            return 0L;
        } catch (Exception e) {
            Log.e(MainActivity.TAG, e.getMessage(), e);
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        mainActivity.showMessage(messageRetour);
    }
}