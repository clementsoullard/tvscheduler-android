package com.clement.tvscheduler.task;

import android.os.AsyncTask;

import com.clement.tvscheduler.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Clément on 09/07/2016.
 */
public class CreditTask extends AsyncTask<Integer, Integer, Long> {


    private MainActivity mainActivity;

    private String messageRetour;

    public CreditTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://192.168.1.20/tvscheduler/credit?value=" + params[0]).openConnection();
            urlConnection.getContent();
            messageRetour="Succès";
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