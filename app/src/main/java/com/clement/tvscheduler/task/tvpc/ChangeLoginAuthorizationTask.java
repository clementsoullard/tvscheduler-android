package com.clement.tvscheduler.task.tvpc;

import android.util.Log;

import com.clement.tvscheduler.activity.TvPcActivity;
import com.clement.tvscheduler.task.BaseTask;

import java.net.HttpURLConnection;

/**
 * Created by Clément on 09/07/2016.
 */
public class ChangeLoginAuthorizationTask extends BaseTask {


    private String messageRetour;

    private Boolean enable;

    /**
     *
     *
     */

    public ChangeLoginAuthorizationTask(TvPcActivity tvPcActivity, Boolean enable) {
        super(tvPcActivity);
        this.enable = enable;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            HttpURLConnection urlConnection = getHttpUrlConnection("tvscheduler/pc-activate/" + enable);
            urlConnection.getContent();
            messageRetour = "Succès";
            return 0L;
        } catch (Exception e) {
            Log.e(TvPcActivity.TAG, "Erreur " + e.getMessage());
        }
        messageRetour = "Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        connectedActivity.showMessage(messageRetour);
    }
}