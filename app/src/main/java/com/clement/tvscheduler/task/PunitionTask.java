package com.clement.tvscheduler.task;

import android.content.ContentValues;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class PunitionTask extends BaseTask {


    private String messageRetour;

    private String baserUrl;

    private Integer punition;


    public PunitionTask(MainActivity mainActivity,Integer punition) {
        super(mainActivity);
        this.punition=punition;

    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/punition");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
             */

         JSONObject root = new JSONObject();
            root.put("value", punition);
            root.put("advertisementId", "desobeissance");

            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            messageRetour = "Succès";

            int responseCode = urlConnection.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_OK) {
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