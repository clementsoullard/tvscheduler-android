package com.clement.tvscheduler.task;

import android.content.ContentValues;
import android.os.AsyncTask;
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
public class PunitionTask extends AsyncTask<Integer, Integer, Long> {


    private MainActivity mainActivity;

    private String messageRetour;

    public PunitionTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://192.168.1.20/tvscheduler/punition").openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            Log.e(MainActivity.TAG, "11 - url :http://192.168.1.20/tvscheduler/punition ");

            /*
             * JSON
             */


            JSONObject root = new JSONObject();
           try {
               root.put("value", new Integer(params[0]));
               root.put("advertisementId", "desobeissance");

               String str = root.toString();
               byte[] outputBytes = str.getBytes("UTF-8");
               OutputStream os = urlConnection.getOutputStream();
               os.write(outputBytes);
           }catch(Exception e){
               Log.e(ContentValues.TAG,e.getMessage());
           }
            messageRetour="Succès";

            int responseCode = urlConnection.getResponseCode();

            Log.e(MainActivity.TAG, "13 - responseCode : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.e(MainActivity.TAG, "14 - HTTP_OK");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    messageRetour += line;
                }
            } else {
                Log.e(MainActivity.TAG, "14 - False - HTTP_OK");
                messageRetour = "";
            }
            return 0L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageRetour="Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        mainActivity.credited(messageRetour);
    }
}