package com.clement.tvscheduler.task;

import android.util.Log;

import com.clement.tvscheduler.activity.MainActivity;

import java.net.HttpURLConnection;

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
            HttpURLConnection urlConnection = getHttpUrlConnection( "tvscheduler/credit?value=" + credit);
            urlConnection.getContent();
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
    }
}