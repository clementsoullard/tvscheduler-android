package com.clement.tvscheduler.task;

import android.util.Log;

import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.activity.MainActivity;
import com.clement.tvscheduler.object.Achat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This task is to add an achat to the liste de courses
 * Created by Clément on 09/07/2016.
 */
public class AddAchatTask extends BaseTask {


    private String messageRetour;

    private Achat achat;

    private ListeCourseActivity listeCourseActivity;
    /**
     *
     *
     */

    public AddAchatTask(ListeCourseActivity listCourseActivity, Achat achat) {
        super(listCourseActivity);
        this.listeCourseActivity=listCourseActivity;
        this.achat = achat;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/ws-create-achat");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
          /*
             * JSON
             */
            JSONObject root = new JSONObject();
            root.put("name", achat.getName());
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
        listeCourseActivity.achatEnregistre();
    }
}