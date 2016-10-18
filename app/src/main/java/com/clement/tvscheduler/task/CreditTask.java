package com.clement.tvscheduler.task;

import android.os.AsyncTask;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Clément on 09/07/2016.
 */
public class CreditTask extends BaseTask {



    private String messageRetour;
      private Integer credit;

    /**
     *
     *
     */

    public CreditTask(MainActivity mainActivity, Integer credit) {
        super(mainActivity);
          this.credit = credit;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL( getBaseURL()+ "tvscheduler/credit?value=" + credit).openConnection();
            urlConnection.getContent();
            messageRetour = "Succès";
            return 0L;
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        mainActivity.showMessage(messageRetour);
    }
}