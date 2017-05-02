package com.clement.tvscheduler.task.tvpc;

import android.util.JsonReader;
import android.util.Log;

import com.clement.tvscheduler.activity.TvPcActivity;
import com.clement.tvscheduler.object.TVStatus;
import com.clement.tvscheduler.task.BaseTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Clément on 09/07/2016.
 */
public class TVStatusTask extends BaseTask {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    TVStatus tvStatus;
    private String messageRetour;

    TvPcActivity tvPcActivity;

    public TVStatusTask(TvPcActivity tvPcActivity) {

        super(tvPcActivity);
        this.tvPcActivity = tvPcActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(TvPcActivity.TAG, "Execution " + this.getClass());
            InputStream is = getHttpUrlConnection("/tvscheduler/tvstatus").getInputStream();
            tvStatus = readJsonStream(is);
            messageRetour = "Succès";
            Log.i(TvPcActivity.TAG, "Execution après");
            return 0L;
        } catch (Exception e) {
            Log.e(TvPcActivity.TAG, e.getMessage());
        }
        messageRetour = "Service non disponible";
        return 0L;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        if (tvStatus != null) {
            tvPcActivity.setTimeRemaining(tvStatus.getRemainingTime());
            if (tvStatus.getStatusRelay()) {
                tvPcActivity.setRelayStatus("Télé autorisée");
            } else {
                tvPcActivity.setRelayStatus("Télé interdite");
            }
            if (tvStatus.getNextCreditOn() != null) {
                tvPcActivity.setNextCredit(tvStatus.getNextCreditOn(), tvStatus.getNextCreditAmount());
            }
            if (tvStatus.getConsumedToday() != null) {
                tvPcActivity.setConsumedToday(tvStatus.getConsumedToday());
            }
            if (tvStatus.getActiveTV()) {
                tvPcActivity.setTvStatus("La télé est ON");
            } else {
                tvPcActivity.setTvStatus("La télé est OFF");
            }
        } else {
            connectedActivity.showMessage("Impossible de se connecter au serveur");
        }


    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    public TVStatus readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readTvStatus(reader);
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
    public TVStatus readTvStatus(JsonReader reader) throws IOException {
        Log.d(TvPcActivity.TAG, "Decryptage du TV status");

        TVStatus tvStatus = new TVStatus();

        String remainingTime = null;
        Date nextCredit = null;
        Integer nextAmount = null;
        String timeToday = "-";
        Boolean relayStatus = false;
        Boolean activeTv = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.i(TvPcActivity.TAG, "Decryptage du TV status entry name " + name);

            if (name.equals(TVStatus.REMAINING_TIME)) {
                remainingTime = reader.nextString();
            } else if (name.equals(TVStatus.RELAY_STATUS)) {
                relayStatus = reader.nextBoolean();
            } else if (name.equals(TVStatus.DATE_OF_CREDIT)) {
                String dateInt = reader.nextString();
                Log.i(TvPcActivity.TAG, "Lecture de la date " + dateInt);
                try {
                    nextCredit = df.parse(dateInt);
                } catch (ParseException e) {
                    Log.i(TvPcActivity.TAG, e.getMessage(), e);

                }
            } else if (name.equals(TVStatus.TIME_CONSUMED_TODAY)) {
                String minutesInt = reader.nextString();
                Log.i(TvPcActivity.TAG, "Lecture des minutes " + minutesInt);
                timeToday = "Minutes consommées: " + minutesInt;
            } else if (name.equals(TVStatus.AMOUNT_OF_CREDIT_IN_MINUTES)) {
                nextAmount = reader.nextInt();
            } else if (name.equals(TVStatus.ACTIVE_STANDBY_STATE)) {
                activeTv = (reader.nextInt() != 1);
            } else {
                reader.skipValue();
            }
        }
        tvStatus.setRemainingSecond(remainingTime);
        tvStatus.setStatusRelay(relayStatus);
        tvStatus.setNextCreditOn(nextCredit);
        tvStatus.setNextCreditAmount(nextAmount);
        tvStatus.setConsumedToday(timeToday);
        tvStatus.setActiveTV(activeTv);

        reader.endObject();
        return tvStatus;
    }


}